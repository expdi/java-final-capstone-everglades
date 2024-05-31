package com.expeditors.trackservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.StringTemplate.STR;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Track extends Entity {

    private String title;
    private String album;
    private double durationInMinutes;
    private MediaType type;
    private Set<Artist> artistList;
    private LocalDate issueDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;

        return  Objects.equals(getId(), track.getId()) &&
                Double.compare(durationInMinutes, track.durationInMinutes) == 0 &&
                Objects.equals(title, track.title) &&
                Objects.equals(album, track.album) &&
                Objects.equals(issueDate, track.issueDate) &&
                type.equals(track.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, album, issueDate, durationInMinutes, type);
    }

    @Override
    public String toString() {
        return STR."Track{title= \{title} }";
    }
}
