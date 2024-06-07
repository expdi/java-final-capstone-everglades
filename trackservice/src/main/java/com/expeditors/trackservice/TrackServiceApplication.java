package com.expeditors.trackservice;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.service.TrackService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.expeditors.trackservice.config.profiles.Profiles.RUNNER;

@SpringBootApplication
public class TrackServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrackServiceApplication.class, args);
    }
}
