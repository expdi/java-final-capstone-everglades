package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServiceUtil {

    public static List<Track> generateTracks(){
        return new ArrayList<>(List.of(

                Track.builder()
                        .durationInMinutes(1.4)
                        .album("The Tortured Poets")
                        .issueDate(LocalDate.of(2023, Month.APRIL, 19))
                        .title("Fortnight")
                        .type(MediaType.MP3)
                        .artistList(Set.of(
                                Artist.builder()
                                        .firstName("Taylor")
                                        .lastName("Swift")
                                        .build(),
                                Artist.builder()
                                        .firstName("Pink")
                                        .lastName("Floyd")
                                        .build()
                        ) )
                        .build(),

                Track.builder()
                        .durationInMinutes(2)
                        .album("The Tortured Poets")
                        .issueDate(LocalDate.of(2023, Month.APRIL, 19))
                        .title("My Boy Only Breaks")
                        .type(MediaType.OGG)
                        .artistList(Set.of())
                        .build(),

                Track.builder()
                        .durationInMinutes(4.21)
                        .album("The Tortured Poets")
                        .issueDate(LocalDate.of(2023, Month.APRIL, 19))
                        .title("Down Bad")
                        .type(MediaType.OGG)
                        .artistList(Set.of(
                                Artist.builder()
                                        .firstName("Jennifer")
                                        .lastName("Lopez")
                                        .build()
                        ))
                        .build()
        ));
    }

    public static List<Artist> generateArtists(){
        return new ArrayList<Artist>(List.of(
                Artist.builder()
                        .firstName("Taylor")
                        .lastName("Swift")
                        .build(),
                Artist.builder()
                        .firstName("Pink")
                        .lastName("Floyd")
                        .build(),
                Artist.builder()
                        .firstName("Jennifer")
                        .lastName("Lopez")
                        .build()
        ));
    }
}
