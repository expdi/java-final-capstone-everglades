package com.expeditors.trackservice.repository.jpa;

import com.expeditors.trackservice.domain.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.expeditors.trackservice.config.profiles.Profiles.H2;
import static com.expeditors.trackservice.config.profiles.Profiles.JPA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@ActiveProfiles(profiles = {JPA, H2})
public class TrackJpaRepositoryAdapterTest {

    @Autowired
    TrackJpaRepositoryAdapter repo;

    @Test
    void findAll_ShouldRunSuccessful(){

        var tracks = repo.getAllEntities();
        assertThat(tracks.size()).isEqualTo(3);
    }

    @Test
    void findById_ShouldRunSuccessful_WhenIdIsCorrect(){

        var track = repo.getEntityById(1);
        assertAll(
                () -> assertThat(track).isNotEmpty(),
                () -> assertThat(track.get().getId()).isEqualTo(1)
        );
    }

    @Test
    void findById_ShouldRunSuccessful_WhenIdIsIncorrect(){

        var track = repo.getEntityById(50);
        assertThat(track).isEmpty();
    }

   @Test
   void getTracksByMediaType_ShouldRunSuccessful() {

       var trackList = repo.getTracksByMediaType(MediaType.MP3);

       assertAll(
               () -> assertThat(trackList.size()).isEqualTo(1),
               () -> assertThat(trackList.getFirst().getType()).isEqualTo(MediaType.MP3)
       );
   }
}
