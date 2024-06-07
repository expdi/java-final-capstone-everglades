package com.expeditors.trackservice.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TrackBuilder {

    private int id;
    private String title;
    private String album;
    private double durationInMinutes;
    private MediaType type;
    private Set<Artist> artistList = new HashSet<>();
    private LocalDate issueDate;


    public Track build(){
        return new Track(
                id,
                title,
                album,
                durationInMinutes,
                type,
                artistList,
                issueDate
        );
    }

    public TrackBuilder id(int id) {
        this.id = id;
        return this;
    }

    public TrackBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TrackBuilder album(String album) {
        this.album = album;
        return this;
    }

    public TrackBuilder durationInMinutes(double durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public TrackBuilder type(MediaType type) {
        this.type = type;
        return this;
    }

    public TrackBuilder artistList(Set<Artist> artistList) {
        this.artistList = artistList;
        return this;
    }

    public TrackBuilder issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }
}
