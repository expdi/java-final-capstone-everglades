package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.AbstractEntity;
import com.expeditors.trackservice.domain.Track;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.Month;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static com.expeditors.trackservice.service.ServiceUtil.generateTracks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles(profiles = {JPA, H2, PRICING_PROVIDER_LOCAL })
@Sql(
        scripts = { "/sql/postgres/schema.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class TrackServiceIntegrationTest__JPA extends TrackServiceIntegrationTest {}

@ActiveProfiles(profiles = {IN_MEMORY, PRICING_PROVIDER_LOCAL })
class TrackServiceIntegrationTest__IN_MEMORY extends TrackServiceIntegrationTest{
}


@SpringBootTest(webEnvironment = NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TrackServiceIntegrationTest {

    @Autowired
    protected TrackService service;

    @BeforeEach
    void beforeAll(){
        for (Track track: generateTracks()) {
            service.addEntity(track);
        }
    }

    @AfterEach
    void afterEach(){

        var ids = service.getAllEntities()
                .stream()
                .map(AbstractEntity::getId)
                .toList();

        for (Integer id : ids) {
            service.deleteEntity(id);
        }
    }

    @Test
    void findAll_ShouldRunSuccessful(){

        var trackList = service.getAllEntities();
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(3),

                () -> assertThat(trackList.stream()
                        .map(Track::getTitle)
                        .toList())
                        .containsExactlyInAnyOrder( "Fortnight", "My Boy Only Breaks", "Down Bad" )
        );
    }

    @Test
    void addEntity_ShouldNotAccept_NullBeingPassed(){

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addEntity(null)
        );
    }

    @Test
    void addEntity_ShouldAccept_CorrectEntityBeingPassed(){

        var track = getTrackObj();

        var trackCreated = service.addEntity(track);

        assertAll(
                () -> assertThat(trackCreated.getId()).isNotEqualTo(0),
                () -> assertThat(trackCreated.getTitle()).isEqualTo("Staying Alive"),
                () -> assertThat(trackCreated.getAlbum()).isEqualTo("Saturday Night Fever")
        );
    }

    @Test
    void updateEntity_ShouldAccept_ValidTrack(){

        var id = service
                .getAllEntities()
                .stream()
                .findFirst()
                .get()
                .getId();

        var track = getTrackObj();
        track.setId(id);

        var updated = service.updateEntity(track);
        assertThat(updated).isTrue();
    }


    @Test
    void updateEntity_ShouldAccept_InvalidTrack(){

        var track = getTrackObj();
        track.setId(-1111);

        var updated = service.updateEntity(track);
        assertThat(updated).isFalse();
    }


    @Test
    void updateEntity_ShouldNotAccept_NullBeingPassed(){

        assertThrows(

                IllegalArgumentException.class,
                () -> service.updateEntity(null)
        );
    }

   @Test
   void deleteEntity_ShouldDeleteEntity_WhenIdIsValid(){

        var id = service.getAllEntities()
                .stream()
                .findFirst()
                .get()
                .getId();

        var deleted = service.deleteEntity(id);
        assertThat(deleted).isTrue();
   }

    @Test
    void deleteEntity_ShouldNotDeleteEntity_WhenIdIsNotValid(){

        var deleted = service.deleteEntity(-111);
        assertThat(deleted).isFalse();
    }

    private static Track getTrackObj() {
        var track = Track.builder()
                .issueDate(LocalDate.of(2024, Month.APRIL, 14))
                .durationInMinutes(2.5)
                .title("Staying Alive")
                .album("Saturday Night Fever")
                .build();
        return track;
    }

}
