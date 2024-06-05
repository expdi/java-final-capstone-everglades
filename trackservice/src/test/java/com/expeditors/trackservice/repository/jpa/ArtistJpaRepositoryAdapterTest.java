package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@ActiveProfiles(profiles = {JPA, H2})
public class ArtistJpaRepositoryAdapterTest {

    @Autowired
    ArtistJpaRepositoryAdapter repo;

    @Test
    void findAll_ShouldRunSuccessful(){

        var artists = repo.getAllEntities();
        assertThat(artists.size()).isEqualTo(3);
    }
    private static Artist createArtistInstance() {
        var artist = Artist.builder()
                .firstName("Random")
                .lastName("Artist")
                .build();
        return artist;
    }

    @Test
    void findById_ShouldRunSuccessful_WhenIdIsCorrect(){

        var artist = repo.getEntityById(1);
        assertAll(
                () -> assertThat(artist).isNotEmpty(),
                () -> assertThat(artist.get().getId()).isEqualTo(1)
        );
    }
    @Test
    @Transactional
    void deleteEntity_ShouldRunSuccessful(){

        var trackDeleted = repo.deleteEntity(1);
        assertThat(trackDeleted).isTrue();
    }

    @Test
    @Transactional
    void deleteEntity_ShouldRunSuccessful_WhenIdIsInvalid(){

        var trackDeleted = repo.deleteEntity(-1111);
        assertThat(trackDeleted).isFalse();
    }


}
