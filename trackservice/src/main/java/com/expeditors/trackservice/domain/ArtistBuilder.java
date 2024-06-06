package com.expeditors.trackservice.domain;

import java.util.HashSet;
import java.util.Set;

public class ArtistBuilder {

    private int id;
    private String firstName;
    private String lastName;
    private Set<Track> trackList = new HashSet<>();


    public Artist build(){
        return new Artist(
                id,
                firstName,
                lastName,
                trackList
        );
    }

    public ArtistBuilder id(int id){
        this.id = id;
        return this;
    }

    public ArtistBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ArtistBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ArtistBuilder trackList(Set<Track> trackList) {
        this.trackList = trackList;
        return this;
    }
}
