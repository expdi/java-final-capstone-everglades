package com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.artist;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.taskmanagement.Task;

public class DeleteArtistFromTrackTask implements Task {

    private final Artist artist;
    private final Track track;

    public DeleteArtistFromTrackTask(Artist artist, Track track) {
        this.artist = artist;
        this.track = track;
    }

    @Override
    public boolean process() {
        return track.getArtistList().removeIf(a -> a.getId() == artist.getId());
    }

    @Override
    public boolean revert() {
        return track.getArtistList().add(artist);
    }
}
