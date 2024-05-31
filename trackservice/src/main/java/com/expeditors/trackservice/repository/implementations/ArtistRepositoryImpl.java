package com.expeditors.trackservice.repository.implementations;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.ArtistRepository;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.artist.DeleteArtistFromTrackTask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ArtistRepositoryImpl
        extends AbstractBaseInMemoryRepository<Artist>
        implements ArtistRepository {

    private final TaskManager taskManager;

    public ArtistRepositoryImpl(
            Map<Integer, Artist> artistList,
            TaskManager taskManager) {

        super(artistList);
        this.taskManager = taskManager;
    }

    @Override
    public boolean deleteEntity(int id) {

        var artistToDeleteOpt = getEntityById(id);
        if(artistToDeleteOpt.isEmpty()){
            return false;
        }

        var isArtistDeleted = super.deleteEntity(id);
        if(!isArtistDeleted){
            return false;
        }

        var artistToDelete = artistToDeleteOpt.get();
        artistToDelete
                .getTrackList()
                .forEach(t -> taskManager.addTask(new DeleteArtistFromTrackTask(artistToDelete, t)));

        return taskManager.processTasks();
    }

    @Override
    public List<Artist> getArtistByName(String firstName) {
        return getAllEntities()
                .stream()
                .filter(a -> a.getFirstName().equalsIgnoreCase(firstName))
                .toList();
    }

    @Override
    public List<Track> getTracksByArtistId(int id) {
        var artistOptional = getEntityById(id);

        return artistOptional
                .map(artist -> artist.getTrackList().stream().toList())
                .orElseGet(ArrayList::new);
    }
}
