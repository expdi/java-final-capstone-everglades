package com.expeditors.trackservice.service.implementations;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.TrackRepository;
import com.expeditors.trackservice.service.AbstractBaseService;
import com.expeditors.trackservice.service.TrackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TrackServiceImpl
        extends AbstractBaseService<Track>
        implements TrackService {

    private final TrackRepository repository;

    public TrackServiceImpl(TrackRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<Track> getTracksByMediaType(MediaType mediaType) {
        return repository.getTracksByMediaType(mediaType);
    }

    @Override
    public List<Track> getTracksByYear(int year) {
        return repository.getTracksByYear(year);
    }

    @Override
    public List<Artist> getArtistByTracks(int trackId) {
        return repository.getArtistsByTrack(trackId);
    }

    @Override
    public List<Track> getTracksByDurationGreaterThan(double duration) {
        return repository.getTracksByDurationGreaterThan(duration);
    }

    @Override
    public List<Track> getTracksByDurationEqualsTo(double duration) {
        return repository.getTracksByDurationEqualsTo(duration);
    }

    @Override
    public List<Track> getTracksByDurationLessThan(double duration) {
        return repository.getTracksByDurationLessThan(duration);
    }
}
