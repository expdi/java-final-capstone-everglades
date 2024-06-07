package com.expeditors.trackservice.repository.inmemory.taskmanagement.implementation.tasks.track;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.inmemory.taskmanagement.Task;

public class AddTrackForArtistTask implements Task {

    private final Artist artist;
    private final Track track;

    public AddTrackForArtistTask(Artist artist, Track track) {
        this.artist = artist;
        this.track = track;
    }

    @Override
    public boolean process() {
        return artist.getTrackList().add(track);
    }

    @Override
    public boolean revert() {
        return artist.getTrackList().removeIf(t -> t.getId() == track.getId());
    }
}
