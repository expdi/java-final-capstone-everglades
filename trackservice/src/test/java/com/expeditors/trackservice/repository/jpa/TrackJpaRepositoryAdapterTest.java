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

import static com.expeditors.trackservice.config.profiles.Profiles.H2;
import static com.expeditors.trackservice.config.profiles.Profiles.JPA;
import static com.expeditors.trackservice.config.profiles.Profiles.POSTGRE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
//@ActiveProfiles(profiles = {JPA, H2})
@ActiveProfiles(profiles = {JPA, POSTGRE})
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

    @Test
    void getTracksByYear_ShouldRunSuccessful(){

        var trackList = repo.getTracksByYear(2023);
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(1),
                () -> assertThat(trackList.getFirst().getIssueDate().getYear()).isEqualTo(2023)
        );
   }

    @Test
    void getTracksByDurationGreaterThan_ShouldRunSuccessful(){

        var trackList = repo.getTracksByDurationGreaterThan(2);
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(1),
                () -> assertThat(trackList.getFirst().getDurationInMinutes()).isGreaterThan(2)
        );
   }

    @Test
    void getTracksByDurationEqualsTo_ShouldRunSuccessful(){

        var trackList = repo.getTracksByDurationEqualsTo(2);
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(1),
                () -> assertThat(trackList.getFirst().getDurationInMinutes()).isEqualTo(2)
        );
    }

    @Test
    void getTracksByDurationLessThan_ShouldRunSuccessful(){

        var trackList = repo.getTracksByDurationLessThan(2);
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(1),
                () -> assertThat(trackList.getFirst().getDurationInMinutes()).isLessThan(2.5)
        );
    }

    @Test
    void getArtistsByTrack_ShouldRunSuccessful(){

        var artistList= repo.getArtistsByTrack(1);
        assertAll(
                () -> assertThat(artistList.size()).isEqualTo(2),
                () -> assertThat(
                        artistList .stream()
                        .map(Artist::getFirstName)
                        .toList()
                ).containsExactlyInAnyOrder("Pink", "Taylor")
        );
    }

    @Test
    @Transactional
    void addEntity_ShouldRunSuccessful_WhenValidEntity(){

        var track = createTrackInstance();
        var trackCreated = repo.addEntity(track);
        var artistListFromTrackCreated = trackCreated.getArtistList();
        assertAll(
                () -> assertThat(trackCreated.getId()).isNotEqualTo(0),
                () -> assertThat(trackCreated.getArtistList().size()).isEqualTo(2),
                () -> assertThat(artistListFromTrackCreated
                        .stream()
                        .map(Artist::getId).toList())
                        .doesNotContain(0)
        );
    }


    @Test
    @Transactional
    void updateEntity_ShouldRunSuccessful_WhenValidEntity(){

        var track = createTrackInstance();
        track.setId(1);

        var trackUpdated = repo.updateEntity(track);
        assertThat(trackUpdated).isTrue();
    }

    @Test
    @Transactional
    void updateEntity_ShouldRunSuccessful_WhenInvalidEntity(){

        var track = new Track();
        track.setId(-1111);

        var trackUpdated = repo.updateEntity(track);
        assertThat(trackUpdated).isFalse();
    }

    private static Track createTrackInstance() {
        var track = Track.builder()
                .issueDate(LocalDate.of(2024, Month.OCTOBER, 12))
                .durationInMinutes(2.5)
                .album("High School")
                .artistList(Set.of(

                        Artist.builder()
                                .firstName("Wiz")
                                .lastName("Khalifa")
                                .build(),
                        Artist.builder()
                                .firstName("Snoop")
                                .lastName("Dogg")
                                .build()
                ))
                .build();
        return track;
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
