package com.expeditors.trackservice.dto;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.service.PricingProvider;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackResponse {

    private int id;
    private double durationInMinutes;
    private double price;
    private String title;
    private String album;
    private MediaType type;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;
    @JsonAlias("artists")
    private List<ArtistResponseForTrack> artistList;


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class ArtistResponseForTrack{
        private int id;
        private String firstName;
        private String lastName;
    }


    public static TrackResponse fromTrack(
            Track track,
            PricingProvider pricingService){

        var artistList = track.getArtistList()
                .stream()
                .map(a -> ArtistResponseForTrack
                        .builder()
                        .id(a.getId())
                        .firstName(a.getFirstName())
                        .lastName(a.getLastName())
                        .build())
                .toList();

        return TrackResponse
                .builder()
                .id(track.getId())
                .durationInMinutes(track.getDurationInMinutes())
                .price(pricingService.getPrice())
                .title(track.getTitle())
                .album(track.getAlbum())
                .type(track.getType())
                .artistList(artistList)
                .issueDate(track.getIssueDate())
                .build();
    }
}
