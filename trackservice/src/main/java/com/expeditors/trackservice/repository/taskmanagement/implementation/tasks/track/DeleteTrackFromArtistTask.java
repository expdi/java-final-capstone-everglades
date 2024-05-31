package com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.track;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.taskmanagement.Task;

import java.util.Optional;

public class DeleteTrackFromArtistTask implements Task {

    private final Artist artist;
    private Track track;

    public DeleteTrackFromArtistTask(Artist artist, Track track) {
        this.artist = artist;
        this.track = track;
    }

    @Override
    public boolean process() {
        return artist.getTrackList().removeIf(t -> t.getId() == track.getId());
    }

    @Override
    public boolean revert() {

        return artist.getTrackList().add(track);
    }
}
