package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.TrackRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.expeditors.trackservice.config.profiles.Profiles.JPA;


@Repository
@Profile(JPA)
public class TrackJpaRepositoryAdapter
        implements TrackRepository {

    private final TrackJpaRepository repo;

    public TrackJpaRepositoryAdapter(TrackJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Track> getAllEntities() {
        return repo.findAllIncludingArtist();
    }

    @Override
    public Optional<Track> getEntityById(int id) {
        return repo.findByIdIncludingArtist(id);
    }

    @Override
    public List<Track> getTracksByMediaType(MediaType mediaType) {

        return repo.findAllByType(mediaType);
    }

    @Override
    public List<Track> getTracksByYear(int year) {
        return repo.findAllByYear(year);
    }

    @Override
    public List<Track> getTracksByDurationGreaterThan(double duration) {
        return repo.findAllByDurationInMinutesGreaterThan(duration);
    }
    @Override
    public List<Track> getTracksByDurationEqualsTo(double duration) {
        return repo.findAllByDurationInMinutesEquals(duration);
    }
    @Override
    public List<Track> getTracksByDurationLessThan(double duration) {
        return repo.findAllByDurationInMinutesLessThan(duration);
    }
    @Override
    public List<Artist> getArtistsByTrack(int trackId) {
        return repo.findByIdIncludingArtist(trackId)
                .map(Track::getArtistList)
                .orElse(Set.of())
                .stream()
                .toList();
    }


    @Override
    public Track addEntity(Track entity) {
        return repo.save(entity);
    }
    @Override
    public boolean updateEntity(Track entity) {

        var hasEntityInDb = repo.existsById(entity.getId());
        if(!hasEntityInDb){
            return false;
        }

        repo.save(entity);
        return true;
    }


    @Override
    public boolean deleteEntity(int id) {

        var hasEntityInDb = repo.existsById(id);
        if(!hasEntityInDb){
            return false;
        }

        repo.deleteById(id);
        return true;
    }
}
