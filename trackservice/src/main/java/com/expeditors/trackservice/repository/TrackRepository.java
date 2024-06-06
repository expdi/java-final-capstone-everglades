package com.expeditors.trackservice.repository;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.BaseRepository;

import java.util.List;

public interface TrackRepository
        extends BaseRepository<Track> {

    List<Track> getTracksByMediaType(MediaType mediaType);
    List<Track> getTracksByYear(int year);
    List<Artist> getArtistsByTrack(int trackId);
    List<Track> getTracksByDurationGreaterThan(double duration);
    List<Track> getTracksByDurationEqualsTo(double duration);
    List<Track> getTracksByDurationLessThan(double duration);
}
