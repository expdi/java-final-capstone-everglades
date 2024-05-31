package com.expeditors.trackservice.dto;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistResponse {

    private int id;
    private String firstName;
    private String lastName;

    @JsonAlias("tracks")
    private List<TrackResponseForArtist> trackList;


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class TrackResponseForArtist{

        private int id;
        private double durationInMinutes;
        private double price;
        private String title;
        private String album;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate issueDate;

        private MediaType type;
    }


    public static ArtistResponse fromArtist(
            Artist artist,
            double price){

        var tracksList = artist.getTrackList()
                .stream()
                .map(t -> TrackResponseForArtist
                        .builder()
                        .id(t.getId())
                        .durationInMinutes(t.getDurationInMinutes())
                        .price(price)
                        .title(t.getTitle())
                        .album(t.getAlbum())
                        .type(t.getType())
                        .issueDate(t.getIssueDate())
                        .build())
                .toList();

        return ArtistResponse
                .builder()
                .id(artist.getId())
                .firstName(artist.getFirstName())
                .lastName(artist.getLastName())
                .trackList(tracksList)
                .build();

    }
}
