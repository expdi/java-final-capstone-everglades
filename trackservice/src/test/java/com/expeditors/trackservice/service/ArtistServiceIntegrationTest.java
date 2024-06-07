package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Artist;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.expeditors.trackservice.service.ServiceUtil.generateArtists;
import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles(profiles = {JPA, PRICING_PROVIDER_LOCAL, H2})
@Sql(
        scripts = "/sql/postgres/schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class ArtistJpaServiceIntegrationTest extends ArtistServiceIntegrationTest {}

@ActiveProfiles(profiles = {IN_MEMORY, PRICING_PROVIDER_LOCAL})
class ArtistInMemoryServiceIntegrationTest extends ArtistServiceIntegrationTest{}

@SpringBootTest(webEnvironment = NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ArtistServiceIntegrationTest {

    @Autowired
    protected ArtistService service;

    @BeforeAll
    void beforeAll(){
        for (Artist artist: generateArtists()) {
            service.addEntity(artist);
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

        var artistList = service.getAllEntities();
        assertAll(
                () -> assertThat(artistList.size()).isEqualTo(3),

                () -> assertThat(artistList.stream()
                        .map(Artist::getFirstName)
                        .toList())
                        .containsExactlyInAnyOrder( "Jennifer", "Pink", "Taylor" )
        );
    }
}

