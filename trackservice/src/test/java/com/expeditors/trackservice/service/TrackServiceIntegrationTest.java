package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Track;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static com.expeditors.trackservice.service.ServiceUtil.generateTracks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles(profiles = {JPA, H2 })
@Sql(
        scripts = { "/sql/postgres/schema.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class TrackServiceIntegrationTest__JPA extends TrackServiceIntegrationTest {}

@ActiveProfiles(profiles = {IN_MEMORY })
class TrackServiceIntegrationTest__IN_MEMORY extends TrackServiceIntegrationTest{
}


@SpringBootTest(webEnvironment = NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TrackServiceIntegrationTest {

    @Autowired
    protected TrackService service;

    @BeforeAll
    void beforeAll(){
        for (Track track: generateTracks()) {
            service.addEntity(track);
        }
    }

    @Test
    void addEntity_ShouldNotAccept_NullBeingPassed(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addEntity(null)
        );
    }

    @Test
    void findAll(){

        var trackList = service.getAllEntities();
        assertAll(
                () -> assertThat(trackList.size()).isEqualTo(3),

                () -> assertThat(trackList.stream()
                        .map(Track::getTitle)
                        .toList())
                        .containsExactlyInAnyOrder( "Fortnight", "My Boy Only Breaks", "Down Bad" )
        );
    }

}
