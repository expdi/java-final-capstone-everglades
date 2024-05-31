package com.expeditors.trackservice.controller;

import com.expeditors.trackservice.controllers.ArtistController;
import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.dto.ArtistRequest;
import com.expeditors.trackservice.service.ArtistService;
import com.expeditors.trackservice.service.PricingProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerUnitTest {

    @Mock
    ArtistService artistService;
    @Mock
    PricingProvider pricingProvider;

    @InjectMocks
    ArtistController artistController;

    private final LocalDate date1 = LocalDate.now().minusYears(1);
    private final LocalDate date2 = LocalDate.now().minusYears(2);

    private final Track track1 = Track.builder().issueDate(date1).type(MediaType.WAV).album("A1").title("T1").durationInMinutes(2).artistList(new HashSet<>()).build();
    private final Track track2 = Track.builder().issueDate(date2).type(MediaType.MP3).album("A2").title("T2").durationInMinutes(2).artistList(new HashSet<>()).build();

    private final Artist artist1 = Artist.builder().firstName("F1").lastName("L1").trackList(new HashSet<>()).build();
    private final Artist artist2 = Artist.builder().firstName("F2").lastName("L2").trackList(new HashSet<>()).build();


    @Test
    void getAllArtists_ReturnsSuccessfully(){

        Mockito.doReturn(List.of(artist1, artist2))
                .when(artistService).getAllEntities();

        var response = artistController.getAllArtists();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getEntityById_ReturnsNotFound_WhenIdIsNotFound(){

        setUpMockForEntityById(Optional.empty());

        var response = artistController.getArtistById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getEntityById_ReturnsOk_WhenIdIsFound(){

        setUpMockForEntityById(Optional.of(artist1));

        var response = artistController.getArtistById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getArtistByName_ReturnsOk(){

        Mockito.doReturn(List.of(artist1, artist2))
                .when(artistService).getArtistByName(anyString());

        var response = artistController.getArtistByName("This is a test!");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTrackForArtist_ReturnsOk(){

        Mockito.doReturn(List.of(track1, track2))
                .when(artistService).getTracksByArtistId(anyInt());

        var response = artistController.getTrackForArtist(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addArtist_ReturnsOk(){
        var artistRequest = ArtistRequest.builder().firstName("A1").build();

        setUpMockForPricingService();
        Mockito.doReturn(artist1)
                .when(artistService).addEntity(any());

        var response = artistController.addArtist(artistRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateArtist_ReturnsNotFound_WhenIdIsNotFound(){
        var artistRequest = ArtistRequest.builder().firstName("A1").build();

        setUpMockForEntityById(Optional.empty());

        var response = artistController.updateArtist(1, artistRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateArtist_ReturnsInternalServerError_WhenUpdateFails(){
        var artistRequest = ArtistRequest.builder().firstName("A1").build();

        setUpMockForEntityById(Optional.of(artist1));
        setUpMockForUpdateEntity(false);

        var response = artistController.updateArtist(1, artistRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateArtist_ReturnsOk_WhenIdIsFoundAndUpdateWasSuccessful(){
        var artistRequest = ArtistRequest.builder().firstName("A1").build();

        setUpMockForEntityById(Optional.of(artist1));
        setUpMockForUpdateEntity(true);

        var response = artistController.updateArtist(1, artistRequest);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteArtist_ReturnsNotFound_WhenIdIsNotFound(){

        setUpMockForEntityById(Optional.empty());

        var response = artistController.deleteArtist(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteArtist_ReturnsInternalServerError_WhenDeletionFails(){

        setUpMockForEntityById(Optional.of(artist1));
        setUpMockForDeleteEntity(false);

        var response = artistController.deleteArtist(1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private void setUpMockForDeleteEntity(boolean toBeReturned) {
        Mockito.doReturn(toBeReturned)
                .when(artistService).deleteEntity(anyInt());
    }

    @Test
    void deleteArtist_ReturnsNoContent_WhenDeletionIsSuccessful(){

        setUpMockForEntityById(Optional.of(artist1));
        setUpMockForDeleteEntity(true);

        var response = artistController.deleteArtist(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




    private void setUpMockForUpdateEntity(boolean toBeReturned) {
        Mockito.doReturn(toBeReturned)
                .when(artistService).updateEntity(any());
    }

     void setUpMockForEntityById(Optional<Artist> toBeReturned){
        Mockito.doReturn(toBeReturned)
                .when(artistService).getEntityById(anyInt());
    }

    private void setUpMockForPricingService() {
        Mockito.doReturn(2.0)
                .when(pricingProvider).getPrice();
    }

}
