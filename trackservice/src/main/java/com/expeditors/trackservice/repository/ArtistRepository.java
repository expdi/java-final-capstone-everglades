package com.expeditors.trackservice.repository;

import java.util.List;
import java.util.Optional;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.BaseRepository;

public interface ArtistRepository
        extends BaseRepository<Artist> {


    List<Artist> getArtistByName(String firstName);
    List<Track> getTracksByArtistId(int id);



}
