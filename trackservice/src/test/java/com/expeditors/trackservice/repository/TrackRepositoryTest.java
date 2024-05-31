package com.expeditors.trackservice.repository;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.implementations.TrackRepositoryImpl;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TrackRepositoryTest {

    @MockBean
    Map<Integer, Track> tracksMap;

    @Autowired
    TaskManager manager;

    @Autowired
    TrackRepositoryImpl repository;

    private Artist artist1;
    private Artist artist2;
    private Track track1;
    private Track track2;
    private Set<Artist> artistList;
    private Set<Track> trackList;


    @BeforeEach
    void init(){
        artist1 = Artist.builder()
                .id(1)
                .firstName("Antonio")
                .lastName("Nazco")
                .trackList(new HashSet<>())
                .build();

        artist2 = Artist.builder()
                .id(2)
                .firstName("Nathaly")
                .lastName("Nazco")
                .trackList(new HashSet<>())
                .build();

        artistList = Set.of(artist1);

        track1 = Track.builder()
                .id(1)
                .title("One Day !!!")
                .album("XXI")
                .type(MediaType.MP3)
                .artistList(new HashSet<>(artistList))
                .build();

        track2 = Track.builder()
                .id(2)
                .title("Second Day !!!")
                .album("XXI")
                .type(MediaType.MP3)
                .artistList(new HashSet<>(artistList))
                .build();

        trackList = Set.of(track1, track2);

        artist1.getTrackList().addAll(trackList);
    }


    @Test
    void getAllEntities_ExecutedSuccessfully(){

        Mockito.doReturn(trackList)
                .when(tracksMap).values();

        var trackReturned = repository.getAllEntities();

        assertThat(trackReturned, hasSize(2));
        assertThat(trackReturned,containsInAnyOrder(track1, track2));
    }


    @Test
    void updateEntity_ThrowsIllegalArgumentException_WhenEntityIsNull(){

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.updateEntity(null)
        );
    }

    @Test
    void updateEntity_ReturnsTrue_WithValidObject(){

        var newTrack = Track.builder()
                .id(1)
                .album("New Album")
                .title("First Song!")
                .type(MediaType.MP3)
                .durationInMinutes(5)
                .artistList(new HashSet<>(List.of(artist2)))
                .build();

        Mockito.doReturn(newTrack)
                .when(tracksMap)
                .computeIfPresent(anyInt(), any());
        Mockito.doReturn(true)
                .when(tracksMap)
                .containsKey(anyInt());
        Mockito.doReturn(track1)
                .when(tracksMap)
                .get(anyInt());

        assertTrue( repository.updateEntity(newTrack));

        var tracksFromArtist = artist2.getTrackList();
        assertThat(tracksFromArtist, hasSize(1));
        assertThat(tracksFromArtist, containsInAnyOrder(newTrack));

        Mockito.verify(tracksMap)
                .computeIfPresent(anyInt(),any());
    }


    @Test
    void deleteEntity_ReturnsFalse_WhenIdIsNotFound(){
        Mockito.doReturn(false)
                .when(tracksMap)
                .containsKey(anyInt());

        assertFalse(repository.deleteEntity(1));
    }

    @Test
    void deleteEntity_ReturnsTrue_WhenIdIsFound(){
        Mockito.doReturn(true)
                .when(tracksMap)
                .containsKey(anyInt());
        Mockito.doReturn(track1)
                .when(tracksMap)
                .get(anyInt());
        Mockito.doReturn(track1)
                .when(tracksMap)
                .remove(anyInt());

        assertTrue(repository.deleteEntity(1));

        var tracksFromArtist = artist1.getTrackList();
        assertThat(tracksFromArtist, hasSize(1));
        assertThat(tracksFromArtist, containsInAnyOrder(track2));
    }


    @Test
    void addEntity_ThrowsIllegalArgumentException_WhenEntityIsNull(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.addEntity(null)
        );
    }

    @Test
    void addEntity_ReturnsCorrectObject_WhenValidEntity(){
        var tracksFromArtist = artist2.getTrackList();
        assertThat(tracksFromArtist, hasSize(0));

        var newTrack = Track
                .builder()
                .type(MediaType.MP3)
                .title("Another Day!")
                .album("One Song")
                .artistList(new HashSet<>(List.of(artist2)))
                .build();

        assertNotNull(repository.addEntity(newTrack));
        assertEquals(MediaType.MP3, newTrack.getType());
        assertEquals("Another Day!", newTrack.getTitle());
        assertEquals("One Song", newTrack.getAlbum());

        assertThat(newTrack.getArtistList(), hasSize(1));
        assertThat(newTrack.getArtistList(), containsInAnyOrder(artist2));

        assertThat(tracksFromArtist, hasSize(1));
    }

    @Test
    void getTaskByMediaType_ReturnsListOfTracks_WhenCriteriaFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).type(MediaType.MP3).build(),
                Track.builder().id(2).type(MediaType.OGG).build(),
                Track.builder().id(3).type(MediaType.WAV).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByMediaType(MediaType.MP3);

        assertThat(listReturned, hasSize(1));
        assertEquals(1, listReturned.get(0).getId());
        assertEquals(MediaType.MP3, listReturned.get(0).getType());
    }

    @Test
    void getTaskByMediaType_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).type(MediaType.WAV).build(),
                Track.builder().id(2).type(MediaType.OGG).build(),
                Track.builder().id(3).type(MediaType.WAV).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByMediaType(MediaType.MP3);
        assertThat(listReturned, hasSize(0));
    }

    @Test
    void getTracksByYear_ReturnsListOfTracks_WhenCriteriaFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).issueDate(LocalDate.now()).build(),
                Track.builder().id(2).issueDate(LocalDate.now()).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByYear(2024);
        assertThat(listReturned, hasSize(2));
    }

    @Test
    void getTracksByYear_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).issueDate(LocalDate.now()).build(),
                Track.builder().id(2).issueDate(LocalDate.now()).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByYear(2023);
        assertThat(listReturned, hasSize(0));
    }

    @Test
    void getArtistByTrack_ReturnsListOfArtists_WhenCriteriaFindMatches(){

        Mockito.doReturn(true)
                .when(tracksMap)
                .containsKey(anyInt());
        Mockito.doReturn(track1)
                .when(tracksMap)
                .get(anyInt());

        var listReturned = repository.getArtistsByTrack(1);
        assertThat(listReturned, hasSize(1));
        assertThat(listReturned, containsInAnyOrder(artist1));
    }

    @Test
    void getTracksByArtist_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).artistList(artistList).build(),
                Track.builder().id(2).artistList(Set.of(artist2)).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getArtistsByTrack(3);
        assertThat(listReturned, hasSize(0));
    }

    @Test
    void getTracksByDurationGreaterThan_ReturnsListOfTracks_WhenCriteriaFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationGreaterThan(2);
        assertThat(listReturned, hasSize(1));
    }

    @Test
    void getTracksByDurationGreaterThan_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationGreaterThan(4);
        assertThat(listReturned, hasSize(0));
    }

    @Test
    void getTracksByDurationLessThan_ReturnsListOfTracks_WhenCriteriaFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationLessThan(3);
        assertThat(listReturned, hasSize(1));
    }

    @Test
    void getTracksByDurationLessThan_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationLessThan(0);
        assertThat(listReturned, hasSize(0));
    }

    @Test
    void getTracksByDurationEqualsTo_ReturnsListOfTracks_WhenCriteriaFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationEqualsTo(2);
        assertThat(listReturned, hasSize(1));
    }

    @Test
    void getTracksByDurationEqualsTo_ReturnsEmptyListOfTracks_WhenCriteriaCannotFindMatches(){
        var mockListOfTracks = List.of(
                Track.builder().id(1).durationInMinutes(2).build(),
                Track.builder().id(2).durationInMinutes(3).build()
        );

        Mockito.doReturn(mockListOfTracks)
                .when(tracksMap).values();

        var listReturned = repository.getTracksByDurationEqualsTo(0);
        assertThat(listReturned, hasSize(0));
    }

}

