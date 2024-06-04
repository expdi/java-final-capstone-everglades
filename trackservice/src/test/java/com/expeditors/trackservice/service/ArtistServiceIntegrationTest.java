package com.expeditors.trackservice.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.expeditors.trackservice.config.profiles.Profiles.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles(profiles = {JPA, H2})
class ArtistServiceIntegrationTest__JPA extends ArtistServiceIntegrationTest {}

@ActiveProfiles(profiles = {IN_MEMORY})
class ArtistServiceIntegrationTest__IN_MEMORY extends ArtistServiceIntegrationTest{ }

@SpringBootTest(webEnvironment = NONE)
public abstract class ArtistServiceIntegrationTest {


}
