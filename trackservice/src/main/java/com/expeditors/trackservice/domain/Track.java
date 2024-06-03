package com.expeditors.trackservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static java.lang.StringTemplate.STR;
@Entity
public class Track extends AbstractEntity {

    private String title;
    private String album;
    private double durationInMinutes;
    @Enumerated(value=EnumType.STRING)
    private MediaType type;

    @ManyToMany (
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "track_artist",
            joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id")
    )
    private Set<Artist> artistList;
    private LocalDate issueDate;



    public Track() {
    }

    public Track(
            String title,
            String album,
            double durationInMinutes,
            MediaType type,
            Set<Artist> artistList,
            LocalDate issueDate) {

        this.title = title;
        this.album = album;
        this.durationInMinutes = durationInMinutes;
        this.type = type;
        this.artistList = artistList;
        this.issueDate = issueDate;
    }

    public Track(
            int id,
            String title,
            String album,
            double durationInMinutes,
            MediaType type,
            Set<Artist> artistList,
            LocalDate issueDate) {

        super(id);
        this.title = title;
        this.album = album;
        this.durationInMinutes = durationInMinutes;
        this.type = type;
        this.artistList = artistList;
        this.issueDate = issueDate;
    }

    public static TrackBuilder builder(){
        return new TrackBuilder();
    }

    public String getTitle() {
        return title;
    }

    public Track setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Track setAlbum(String album) {
        this.album = album;
        return this;
    }

    public double getDurationInMinutes() {
        return durationInMinutes;
    }

    public Track setDurationInMinutes(double durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public Track setType(MediaType type) {
        this.type = type;
        return this;
    }

    public Set<Artist> getArtistList() {
        return artistList;
    }

    public Track setArtistList(Set<Artist> artistList) {
        this.artistList = artistList;
        return this;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Track setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

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
        return "Track{" +
                "title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", type=" + type +
                ", artistList=" + artistList +
                ", issueDate=" + issueDate +
                ", id=" + id +
                '}';
    }
}
