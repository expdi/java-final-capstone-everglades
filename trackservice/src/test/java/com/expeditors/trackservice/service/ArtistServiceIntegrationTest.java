package com.expeditors.trackservice.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles(profiles = {JPA, H2})
@Sql(
        scripts = "/sql/postgres/schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class ArtistServiceIntegrationTest__JPA extends ArtistServiceIntegrationTest {}

@ActiveProfiles(profiles = {IN_MEMORY})
class ArtistServiceIntegrationTest__IN_MEMORY extends ArtistServiceIntegrationTest{}

@SpringBootTest(webEnvironment = NONE)
public abstract class ArtistServiceIntegrationTest {}
