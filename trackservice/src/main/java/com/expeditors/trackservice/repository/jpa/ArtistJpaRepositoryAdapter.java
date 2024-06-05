package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.ArtistRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("JPA")
public class ArtistJpaRepositoryAdapter implements ArtistRepository {

    private final ArtistJpaRepository repo;

    public ArtistJpaRepositoryAdapter(ArtistJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Artist> getArtistByName(String name) {
        return repo.getArtistByName(name);
    }
    @Override
    public List<Track> getTracksByArtistId(int id) {
        return repo.getTracksByArtistId(id);
    }
    @Override
    public List<Artist> getAllEntities() {
        return repo.findAll();
    }
    @Override
    public Optional<Artist> getEntityById(int id) {
        return repo.findById(id);

    }
    @Override
    public Artist addEntity(Artist entity) {
        return repo.save(entity);
    }
    @Override
    public boolean updateEntity(Artist entity) {

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
