package com.expeditors.trackservice.controller;

import com.expeditors.trackservice.controllers.TrackController;
import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.dto.TrackRequest;
import com.expeditors.trackservice.service.ArtistByIdsReturn;
import com.expeditors.trackservice.service.ArtistService;
import com.expeditors.trackservice.service.PricingProvider;
import com.expeditors.trackservice.service.TrackService;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrackControllerUnitTest {

    @Mock
    PricingProvider pricingProvider;
    @Mock
    TrackService trackService;
    @Mock
    ArtistService artistService;

    @InjectMocks
    TrackController trackController;

    private final LocalDate date1 = LocalDate.now().minusYears(1);
    private final LocalDate date2 = LocalDate.now().minusYears(2);

    private final Track track1 = Track.builder().issueDate(date1).type(MediaType.WAV).album("A1").title("T1").durationInMinutes(2).artistList(new HashSet<>()).build();
    private final Track track2 = Track.builder().issueDate(date2).type(MediaType.MP3).album("A2").title("T2").durationInMinutes(2).artistList(new HashSet<>()).build();

    private final Artist artist1 = Artist.builder().firstName("F1").lastName("L1").trackList(new HashSet<>()).build();
    private final Artist artist2 = Artist.builder().firstName("F2").lastName("L2").trackList(new HashSet<>()).build();


    @Test
    void getAllTracks_RunsSuccessfully() {
        var l1 = List.of(track1, track2);

        setUpMockForPricingService();
        Mockito.doReturn(l1)
                .when(trackService).getAllEntities();

        var response = trackController.getAllTracks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTrackById_ReturnsNotFound(){

        setUpMockForEntityById(Optional.empty());

        var response = trackController.getTrackById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    void getTrackById_ReturnOkAndTrack(){

        track1.getArtistList().add(artist1);
        track1.getArtistList().add(artist2);

        setUpMockForPricingService();
        setUpMockForEntityById(Optional.of(track1));

        var response = trackController.getTrackById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTrackByMediaType_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(track1))
                .when(trackService).getTracksByMediaType(any());

        var response = trackController.getTrackByMediaType(MediaType.WAV);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTracksByYear_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(track1))
                .when(trackService).getTracksByYear(anyInt());

        var response = trackController.getTrackByYear(2024);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTracksByArtist_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(artist1))
                .when(trackService).getArtistByTracks(anyInt());

        var response = trackController.getArtistByTrackId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTracksByDurationGreaterThan_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(track1))
                .when(trackService).getTracksByDurationGreaterThan(anyDouble());

        var response = trackController.getTracksByDurationGreaterThan(1.0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTracksByDurationLessThan_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(track1))
                .when(trackService).getTracksByDurationLessThan(anyDouble());

        var response = trackController.getTracksByDurationLessThan(1.0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTracksByDurationEqualsTo_RunsSuccessfully(){

        setUpMockForPricingService();
        Mockito.doReturn(List.of(track1))
                .when(trackService).getTracksByDurationEqualsTo(anyDouble());

        var response = trackController.getTracksByDurationEqualsTo(1.0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAddTrack_ReturnsBadRequest_WhenHasMissingArtists(){

        var artistReturn = new ArtistByIdsReturn(Set.of(1), Set.of());
        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForGetArtistsByListOfIds(artistReturn);

        var response = trackController.addTrack(trackRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addTrack_ReturnsOk_WhenAllArtistAreFound(){
        var artistReturn = new ArtistByIdsReturn(Set.of(), Set.of(artist1, artist2));
        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForGetArtistsByListOfIds(artistReturn);
        setUpMockForPricingService();
        Mockito.doReturn(track1)
                .when(trackService).addEntity(any());

        var response = trackController.addTrack(trackRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTrack_ReturnsNotFound_WhenIdIsNotFound(){

        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForEntityById(Optional.empty());

        var response = trackController.updateTrack(1, trackRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTrack_ReturnsBadRequest_WhenHasArtistIdMissing(){
        var artistReturn = new ArtistByIdsReturn(Set.of(1), Set.of(artist2));
        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForEntityById(Optional.of(track1));
        setUpMockForGetArtistsByListOfIds(artistReturn);

        var response = trackController.updateTrack(1, trackRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTrack_ReturnsInternalServerError_UpdateFailed(){
        var artistReturn = new ArtistByIdsReturn(Set.of(), Set.of(artist1, artist2));
        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForEntityById(Optional.of(track1));
        setUpMockForGetArtistsByListOfIds(artistReturn);
        setUpMockForUpdateEntity(false);

        var response = trackController.updateTrack(1, trackRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateTrack_ReturnsOk_WhenUpdateIsSuccessfulAndIdIsFoundAndArtistAreFound(){
        var artistReturn = new ArtistByIdsReturn(Set.of(), Set.of(artist1, artist2));
        var trackRequest = TrackRequest.builder().title("Hello").artistIds(List.of(1,2)).build();

        setUpMockForEntityById(Optional.of(track1));
        setUpMockForGetArtistsByListOfIds(artistReturn);
        setUpMockForUpdateEntity(true);

        var response = trackController.updateTrack(1, trackRequest);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTrack_ReturnsNotFound_WhenIdIsNotFound(){

        setUpMockForEntityById(Optional.empty());

        var response = trackController.deleteTrack(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTrack_ReturnsInternalServerError_WhenDeletionFails(){

        setUpMockForEntityById(Optional.of(track1));
        setUpMockForDeleteEntity(false);

        var response = trackController.deleteTrack(1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteTrack_ReturnsOk_WhenDeletionIsSuccessful(){

        setUpMockForEntityById(Optional.of(track1));
        setUpMockForDeleteEntity(true);

        var response = trackController.deleteTrack(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




    private void setUpMockForDeleteEntity(boolean toBeReturned) {
        Mockito.doReturn(toBeReturned)
                .when(trackService).deleteEntity(anyInt());
    }


    private void setUpMockForUpdateEntity(boolean returnValue) {
        Mockito.doReturn(returnValue)
                .when(trackService).updateEntity(any());
    }


    private void setUpMockForEntityById(Optional<Track> optTrack) {

        Mockito.doReturn(optTrack)
                .when(trackService).getEntityById(anyInt());
    }

    private void setUpMockForGetArtistsByListOfIds(ArtistByIdsReturn artistReturn) {
        Mockito.doReturn(artistReturn)
                .when(artistService).getArtistsById(anyList());
    }

    private void setUpMockForPricingService() {
        Mockito.doReturn(2.0)
                .when(pricingProvider).getPrice();
    }

}
