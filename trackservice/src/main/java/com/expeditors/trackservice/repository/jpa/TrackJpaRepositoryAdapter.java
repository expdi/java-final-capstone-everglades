package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.TrackRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("JPA")
public class TrackJpaRepositoryAdapter implements TrackRepository {
    @Override
    public List<Track> getAllEntities() {
        return null;
    }

    @Override
    public boolean updateEntity(Track entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(int id) {
        return false;
    }

    @Override
    public Track addEntity(Track entity) {
        return null;
    }

    @Override
    public Optional<Track> getEntityById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Track> getTracksByMediaType(MediaType mediaType) {
        return null;
    }

    @Override
    public List<Track> getTracksByYear(int year) {
        return null;
    }

    @Override
    public List<Artist> getArtistsByTrack(int trackId) {
        return null;
    }

    @Override
    public List<Track> getTracksByDurationGreaterThan(double duration) {
        return null;
    }

    @Override
    public List<Track> getTracksByDurationEqualsTo(double duration) {
        return null;
    }

    @Override
    public List<Track> getTracksByDurationLessThan(double duration) {
        return null;
    }
}
