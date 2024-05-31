package com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.track;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddTrackForArtistTaskTest {

    AddTrackForArtistTask addTrackForArtistTask;

    private Artist artist1;
    private Track track1;

    @BeforeEach
    void init(){
        artist1 = Artist.builder()
                .id(1)
                .firstName("Antonio")
                .lastName("Nazco")
                .trackList(new HashSet<>())
                .build();

        List<Artist> artistList = List.of(artist1);

        track1 = Track.builder()
                .id(1)
                .type(MediaType.WAV)
                .album("One Day")
                .title("Hello!")
                .issueDate(LocalDate.now())
                .artistList(new HashSet<>(artistList))
                .build();
    }

    @Test
    void process_ShouldAddItselfToArtistTrackList_WhenRunsSuccessfully(){
        var task = new AddTrackForArtistTask(artist1, track1);

        assertTrue(task.process());
        assertThat(artist1.getTrackList(), hasSize(1));
        assertThat(artist1.getTrackList(), containsInAnyOrder(track1));
    }

    @Test
    void revert_ShouldRemoveItselfFromArtistTrackList_WhenRunsSuccessfully(){
        artist1.getTrackList().add(track1);
        var task = new AddTrackForArtistTask(artist1, track1);

        assertTrue(task.revert());
        assertThat(artist1.getTrackList(), hasSize(0));

    }
}
