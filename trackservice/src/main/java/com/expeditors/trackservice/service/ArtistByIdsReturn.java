package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Artist;
import lombok.Getter;

import java.util.Set;


public record ArtistByIdsReturn(
        Set<Integer> idOfMissingArtist,
        Set<Artist> artistList) {

    public boolean hasMissingArtists() {
        return !idOfMissingArtist().isEmpty();
    }
}
