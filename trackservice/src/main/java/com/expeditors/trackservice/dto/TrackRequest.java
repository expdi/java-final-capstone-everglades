package com.expeditors.trackservice.dto;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackRequest {

    @NotEmpty
    private String title;
    @NotEmpty
    private String album;
    private double durationInMinutes;
    private MediaType type;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate issueDate;
    private List<Integer> artistIds;


    public Track toTrack(
            Set<Artist> artistList) {

        return Track.builder()
                .title(title)
                .album(album)
                .durationInMinutes(durationInMinutes)
                .type(type)
                .issueDate(issueDate)
                .artistList(artistList)
                .build();
    }
}
