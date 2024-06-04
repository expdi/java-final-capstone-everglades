package com.expeditors.trackservice;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.service.ArtistService;
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
import org.springframework.transaction.annotation.Transactional;

import static com.expeditors.trackservice.config.profiles.Profiles.*;

@SpringBootApplication
public class TrackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackServiceApplication.class, args);
    }


}

@Component
@Profile(RUNNER)
class Runner implements CommandLineRunner{

    private final TrackService trackService;
    private final ArtistService artistService;

    Runner(
            TrackService trackService,
           ArtistService artistService) {

        this.trackService = trackService;
        this.artistService = artistService;
    }
    @Transactional
    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var dateList = List.of(
                LocalDate.now().minusYears(1),
                LocalDate.now().minusYears(2),
                LocalDate.now().minusYears(3)
        );

        var trackList = List.of(
                Track.builder().issueDate(dateList.get(0)).type(MediaType.WAV).album("A1").title("T1").durationInMinutes(2).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(0)).type(MediaType.WAV).album("A2").title("T2").durationInMinutes(2).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(0)).type(MediaType.MP3).album("A3").title("T3").durationInMinutes(3).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(1)).type(MediaType.MP3).album("A4").title("T4").durationInMinutes(3).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(1)).type(MediaType.MP3).album("A5").title("T5").durationInMinutes(3).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(1)).type(MediaType.WAV).album("A6").title("T6").durationInMinutes(3).artistList(new HashSet<>()).build(),
                Track.builder().issueDate(dateList.get(2)).type(MediaType.WAV).album("A7").title("T7").durationInMinutes(4).artistList(new HashSet<>()).build()
        );

        var artistList = List.of(
                Artist.builder().firstName("F1").lastName("L1").trackList(new HashSet<>()).build(),
                Artist.builder().firstName("F2").lastName("L2").trackList(new HashSet<>()).build(),
                Artist.builder().firstName("F3").lastName("L3").trackList(new HashSet<>()).build(),
                Artist.builder().firstName("F4").lastName("L4").trackList(new HashSet<>()).build(),
                Artist.builder().firstName("F5").lastName("L5").trackList(new HashSet<>()).build(),
                Artist.builder().firstName("F6").lastName("L6").trackList(new HashSet<>()).build()
        );

        trackList.get(0).getArtistList().addAll(List.of(artistList.get(1),artistList.get(2)));
        trackList.get(2).getArtistList().addAll(List.of(artistList.get(4),artistList.get(5)));
        trackList.get(4).getArtistList().add(artistList.get(4));

        trackList.forEach(trackService::addEntity);

        List<Track> tracks = trackService.getAllEntities();
        for(Track track : tracks) {
            System.out.println("album: " + track.getAlbum() + ", title: " + track.getTitle() + ", type: " + track.getType());
        }
    }
}
