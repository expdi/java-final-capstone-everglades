package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.MediaType;
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
public interface TrackJpaRepository
        extends JpaRepository<Track, Integer> {

    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.artistList")
    List<Track> findAllIncludingArtist();
    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.artistList WHERE t.id = :id")
    Optional<Track> findByIdIncludingArtist(int id);

//    @Query("SELECT t FROM Track t LEFT JOIN t.artistList WHERE t.type = :type")
    List<Track> findAllByType(MediaType type);

    @Query("SELECT t FROM Track t LEFT JOIN t.artistList WHERE YEAR(t.issueDate) = :year")
    List<Track> findAllByYear(int year);
}
