package com.expeditors.trackservice.repository.implementations;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.TrackRepository;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.track.AddTrackForArtistTask;
import com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.track.DeleteTrackFromArtistTask;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class TrackRepositoryImpl
        extends AbstractBaseInMemoryRepository<Track>
        implements TrackRepository {

    private final TaskManager taskManager;

    public TrackRepositoryImpl(
            Map<Integer, Track> trackList,
            TaskManager taskManager) {

        super(trackList);
        this.taskManager = taskManager;
    }

    @Override
    public boolean updateEntity(Track entity) {
        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("Entity cannot be null");
        }

        var oldTrackOpt = getEntityById(entity.getId());

        var isTrackUpdated = super.updateEntity(entity);
        if(!isTrackUpdated){
            return false;
        }

        var oldTrack = oldTrackOpt.get();

        for (Artist artist : oldTrack.getArtistList()) {
            taskManager.addTask(new DeleteTrackFromArtistTask(artist,oldTrack));
        }
        for (Artist artist : entity.getArtistList()) {
            taskManager.addTask(new AddTrackForArtistTask(artist,entity));
        }
        return taskManager.processTasks();
    }

    @Override
    public boolean deleteEntity(int id) {

        var oldTrackOpt = getEntityById(id);
        if(oldTrackOpt.isEmpty()) {
            return false;
        }

        var oldTrack = oldTrackOpt.get();
        var isTrackDeleted =  super.deleteEntity(id);

        if(!isTrackDeleted){
            return false;
        }

        for (Artist artist : oldTrack.getArtistList()) {
            taskManager.addTask(new DeleteTrackFromArtistTask(artist, oldTrack));
        }

        return taskManager.processTasks();
    }

    @Override
    public Track addEntity(Track entity) {
        var trackAdded = super.addEntity(entity);

        for (Artist artist : trackAdded.getArtistList()) {
            taskManager.addTask(new AddTrackForArtistTask(artist, trackAdded));
        }

        if(taskManager.processTasks()){
            return trackAdded;
        }
        return null;
    }


    private List<Track> getTracksByPredicate(Predicate<Track> predicate){
        return getAllEntities()
                .stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public List<Track> getTracksByMediaType(MediaType mediaType) {
        return getTracksByPredicate(t -> t.getType().equals(mediaType));
    }

    @Override
    public List<Track> getTracksByYear(int year) {
        return getTracksByPredicate(t -> t.getIssueDate().getYear() == year);
    }

    @Override
    public List<Artist> getArtistsByTrack(int trackId) {
        return getEntityById(trackId)
                .map( t -> t.getArtistList().stream().toList())
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Track> getTracksByDurationGreaterThan(double duration) {
        return getTracksByPredicate(t -> t.getDurationInMinutes() > duration);
    }

    @Override
    public List<Track> getTracksByDurationEqualsTo(double duration) {
        return getTracksByPredicate(t -> t.getDurationInMinutes() == duration);
    }

    @Override
    public List<Track> getTracksByDurationLessThan(double duration) {
        return getTracksByPredicate(t -> t.getDurationInMinutes() < duration);
    }
}
