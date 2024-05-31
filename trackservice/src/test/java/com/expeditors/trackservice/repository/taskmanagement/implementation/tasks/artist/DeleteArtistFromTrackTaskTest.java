package com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.artist;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.taskmanagement.implementation.tasks.track.DeleteTrackFromArtistTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteArtistFromTrackTaskTest {

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
    void process_ShouldRemoveArtistFromTrackArtistList_WhenRunsSuccessfully(){
        artist1.getTrackList().add(track1);
        var eTask = new DeleteArtistFromTrackTask(artist1, track1);

        assertTrue(eTask.process());
        assertThat(track1.getArtistList(), hasSize(0));
    }

    @Test
    void revert_ShouldAddArtistToTrackArtistList_WhenRunsSuccessfully(){
        track1.getArtistList().clear();
        var eTask = new DeleteArtistFromTrackTask(artist1, track1);

        assertTrue(eTask.revert());
        assertThat(track1.getArtistList(), hasSize(1));
        assertThat(track1.getArtistList(),containsInAnyOrder(artist1));
    }
}
