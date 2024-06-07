package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.expeditors.trackservice.config.profiles.Profiles.JPA;

@Repository
@Profile(JPA)
public interface ArtistJpaRepository extends JpaRepository<Artist, Integer>{

    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.trackList WHERE a.firstName = :name")
    List<Artist> getArtistByName(String name);

    @Query("SELECT tt FROM Track tt LEFT JOIN FETCH tt.artistList a WHERE a.id = :id")
    List<Track> getTracksByArtistId(int id);
}
