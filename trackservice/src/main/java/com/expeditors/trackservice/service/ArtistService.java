package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;

import java.util.List;
import java.util.Set;

public interface ArtistService extends BaseService<Artist>{

    List<Artist> getArtistByName(String firstName);
    List<Track> getTracksByArtistId(int id);
    ArtistByIdsReturn getArtistsById(List<Integer> idList);
}
