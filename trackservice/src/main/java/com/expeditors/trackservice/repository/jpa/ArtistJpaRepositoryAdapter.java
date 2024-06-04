package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.ArtistRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.expeditors.trackservice.config.profiles.Profiles.JPA;

@Repository
@Profile(JPA)
public class ArtistJpaRepositoryAdapter
            implements ArtistRepository {
    @Override
    public List<Artist> getArtistByName(String firstName) {
        return null;
    }

    @Override
    public List<Track> getTracksByArtistId(int id) {
        return null;
    }

    @Override
    public List<Artist> getAllEntities() {
        return null;
    }

    @Override
    public boolean updateEntity(Artist entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(int id) {
        return false;
    }

    @Override
    public Artist addEntity(Artist entity) {
        return null;
    }

    @Override
    public Optional<Artist> getEntityById(int id) {
        return Optional.empty();
    }
}
