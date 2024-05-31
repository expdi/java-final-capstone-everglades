package com.expeditors.trackservice.repository;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ArtistRepositoryTest {

    Artist artist1;
    Artist artist2;
    Artist artist3;

    @MockBean
    Map<Integer, Artist> artistMap;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    TaskManager taskManager;

    @BeforeEach
    void init(){
        artist1 = Artist.builder()
                .id(1)
                .firstName("Antonio1")
                .lastName("Nazco")
                .trackList(new HashSet<Track>())
                .build();

        artist2 = Artist.builder()
                .id(2)
                .firstName("Antonio2")
                .lastName("Nazco")
                .trackList(new HashSet<Track>())
                .build();

        artist3 = Artist.builder()
                .id(3)
                .firstName("Antonio3")
                .lastName("Nazco")
                .trackList(new HashSet<Track>())
                .build();
    }

    @Test
    void getAllEntities_ShouldRunSuccessful_WhenThereIsRecords(){
        var artistList = List.of(artist1, artist2);

        Mockito.doReturn(artistList)
                        .when(artistMap).values();

        var listReturned = artistRepository.getAllEntities();

        assertThat(listReturned, hasSize(2));
        assertThat(listReturned, containsInAnyOrder(artist1, artist2));
    }

    @Test
    void updateEntity_ShouldThrowIllegalArgumentException_WhenEntityIsNull()
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> artistRepository.updateEntity(null));
    }

    @Test
    void updateEntity_ShouldRunSuccessful_WithValidObject(){

        Mockito.doReturn(artist2)
                .when(artistMap)
                .computeIfPresent(anyInt(), any());

        assertTrue(artistRepository.updateEntity(artist2));
    }

    @Test
    void updateEntity_ShouldFail_WhenUpdateCannotBeMade(){

        Mockito.doReturn(null)
                .when(artistMap)
                .computeIfPresent(anyInt(), any());

        assertFalse(artistRepository.updateEntity(artist2));
    }

    @Test
    void addEntity_ShouldReturnFinalObject_WithValidObject(){

        Mockito.doReturn(artist2)
                .when(artistMap)
                .put(anyInt(), any());

        var finalArtist = artistRepository.addEntity(artist2);
        assertEquals(finalArtist, artist2);
    }

    @Test
    void addEntity_ShouldThrowIllegalArgumentException_WhenEntityIsNull(){
        assertThrows(IllegalArgumentException.class,
                () -> artistRepository.addEntity(null));
    }

    @Test
    void getEntityById_ReturnsEmpty_WhenIdIsNotFound(){

        Mockito.doReturn(false)
                .when(artistMap).containsKey(anyInt());

        var artistReceived = artistRepository.getEntityById(1);
        assertEquals(Optional.empty(), artistReceived);
    }

    @Test
    void getEntityById_ReturnObject_WhenIdIsFound(){

        Mockito.doReturn(true)
                .when(artistMap).containsKey(anyInt());
        Mockito.doReturn(artist2)
                .when(artistMap).get(anyInt());

        var artistReceived = artistRepository.getEntityById(1);
        assertEquals(Optional.of(artist2), artistReceived);
    }

    @Test
    void deleteEntity_ShouldRunSuccessful_WhenIdIsFound(){
        var track = Track.builder()
                        .id(2)
                        .type(MediaType.WAV)
                        .durationInMinutes(3)
                        .artistList(new HashSet<>())
                        .build();
        track.getArtistList().add(artist2);
        artist2.getTrackList().add(track);

        Mockito.doReturn(true)
                .when(artistMap)
                .containsKey(anyInt());
        Mockito.doReturn(artist2)
                .when(artistMap)
                .get(anyInt());
        Mockito.doReturn(artist2)
                .when(artistMap)
                .remove(anyInt());

        assertTrue(artistRepository.deleteEntity(1));
        assertThat(track.getArtistList(), hasSize(0));
    }

    @Test
    void deleteEntity_ShouldRunSuccessful_(){
        var artist = Artist.builder()
                .firstName("F2")
                .lastName("L2")
                .trackList(new HashSet<>())
                .build();

        var track = Track.builder()
                .id(7)
                .durationInMinutes(2.0)
                .title("T1")
                .album("A1")
                .issueDate(LocalDate.now())
                .type(MediaType.WAV)
                .artistList(new HashSet<>())
                .build();

        artist.getTrackList().add(track);
        track.getArtistList().add(artist);

        Mockito.doReturn(true)
                .when(artistMap)
                .containsKey(anyInt());
        Mockito.doReturn(artist)
                .when(artistMap)
                .get(anyInt());
        Mockito.doReturn(track)
                .when(artistMap)
                .remove(anyInt());

        assertTrue(artistRepository.deleteEntity(7));
        assertThat(track.getArtistList(), hasSize(0));
    }

    @Test
    void deleteEntity_ShouldFail_WhenIdIsNotFound(){

        Mockito.doReturn(null)
                .when(artistMap)
                .remove(anyInt());

        assertFalse(artistRepository.deleteEntity(1));
    }

    @Test
    void getArtistByName_RunSuccessful(){
        var expectedList = List.of(artist1, artist2);

        Mockito.doReturn(expectedList)
                .when(artistMap)
                .values();

        var listReturned = artistRepository.getAllEntities();

        assertThat(listReturned, hasSize(2));
        assertThat(listReturned, containsInAnyOrder(artist1, artist2));
    }

    @Test
    void getTracksByArtistId_ReturnsEmpty_WhenIdIsNotFound(){
        Mockito.doReturn(false)
                .when(artistMap)
                .containsKey(anyInt());

        var tracksReturned = artistRepository.getTracksByArtistId(2);
        assertThat(tracksReturned, hasSize(0));
    }

    @Test
    void getTracksByArtistId_ReturnsTracksList_WhenIdIsFound(){

        var track1 = Track.builder()
                .id(1)
                .title("One Day !!!")
                .album("XXI")
                .type(MediaType.MP3)
                .artistList(new HashSet<>())
                .build();

        var track2 = Track.builder()
                .id(2)
                .title("Second Day !!!")
                .album("XXI")
                .type(MediaType.MP3)
                .artistList(new HashSet<>())
                .build();

        var trackList = List.of(track1, track2);
        artist2.getTrackList().addAll(trackList);

        Mockito.doReturn(true)
                .when(artistMap)
                .containsKey(anyInt());
        Mockito.doReturn(artist2)
                .when(artistMap)
                .get(anyInt());

        var tracksReturned = artistRepository.getTracksByArtistId(2);
        assertThat(tracksReturned, hasSize(2));
        assertThat(tracksReturned, containsInAnyOrder(track1, track2));

    }
}
