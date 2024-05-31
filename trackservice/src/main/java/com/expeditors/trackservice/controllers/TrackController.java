package com.expeditors.trackservice.controllers;

import com.expeditors.trackservice.domain.MediaType;
import com.expeditors.trackservice.dto.ArtistResponse;
import com.expeditors.trackservice.dto.ErrorResponse;
import com.expeditors.trackservice.dto.TrackRequest;
import com.expeditors.trackservice.dto.TrackResponse;
import com.expeditors.trackservice.service.ArtistService;
import com.expeditors.trackservice.service.PricingProvider;
import com.expeditors.trackservice.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;
    private final ArtistService artistService;
    private final PricingProvider pricingProvider;

    public TrackController(
            TrackService trackService,
            ArtistService artistService,
            PricingProvider pricingProvider) {

        this.trackService = trackService;
        this.artistService = artistService;
        this.pricingProvider = pricingProvider;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTracks(){
        return ResponseEntity
                .ok()
                .body(
                        trackService.getAllEntities()
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/by/id/{trackId}")
    public ResponseEntity<?> getTrackById(
            @PathVariable int trackId){

       return trackService.getEntityById(trackId)
               .map(t -> ResponseEntity
                       .ok()
                       .body(TrackResponse.fromTrack(t, pricingProvider.getPrice())))
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by/type/{mediaType}")
    public ResponseEntity<?> getTrackByMediaType(
            @PathVariable MediaType mediaType){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getTracksByMediaType(mediaType)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/by/year/{year}")
    public ResponseEntity<?> getTrackByYear(
            @PathVariable int year){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getTracksByYear(year)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/{trackId}/artists")
    public ResponseEntity<?> getArtistByTrackId(
            @PathVariable int trackId){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getArtistByTracks(trackId)
                                .stream()
                                .map(a -> ArtistResponse.fromArtist(a, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/by/duration/greaterthan/{duration}")
    public ResponseEntity<?> getTracksByDurationGreaterThan(
            @PathVariable double duration){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getTracksByDurationGreaterThan(duration)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/by/duration/lessthan/{duration}")
    public ResponseEntity<?> getTracksByDurationLessThan(
            @PathVariable double duration){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getTracksByDurationLessThan(duration)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @GetMapping("/by/duration/equals/{duration}")
    public ResponseEntity<?> getTracksByDurationEqualsTo(
            @PathVariable double duration){

        return ResponseEntity
                .ok()
                .body(
                        trackService.getTracksByDurationEqualsTo(duration)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }

    @PostMapping
    public ResponseEntity<?> addTrack(
            @Valid @RequestBody TrackRequest trackRequest){

        var result = artistService.getArtistsById(trackRequest.getArtistIds());

        if(result.hasMissingArtists()){
           return ResponseEntity
                   .badRequest()
                   .body(ErrorResponse.builder()
                           .message("Id(s) Cannot be Found")
                           .errors(result.idOfMissingArtist().stream().map(String::valueOf).toList())
                           .build()
                   );
        }

        var trackCreated = trackService.addEntity(
                trackRequest.toTrack(result.artistList()));
        return ResponseEntity
                .ok()
                .body(TrackResponse.fromTrack(trackCreated, pricingProvider.getPrice()));
    }

    @PutMapping("/{trackId}")
    public ResponseEntity<?> updateTrack(
            @PathVariable int trackId,
            @Valid @RequestBody TrackRequest trackRequest){

        var trackFromService = trackService.getEntityById(trackId);
        if(trackFromService.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var artistByIdsResult = artistService.getArtistsById(trackRequest.getArtistIds());
        if(artistByIdsResult.hasMissingArtists()){
            return ResponseEntity
                    .badRequest()
                    .body(ErrorResponse.builder()
                            .message("Id(s) Cannot be Found")
                            .errors(artistByIdsResult.idOfMissingArtist().stream().map(String::valueOf).toList())
                            .build()
                    );
        }

        var trackToUpdate = trackRequest.toTrack(artistByIdsResult.artistList());
        trackToUpdate.setId(trackId);

        var isTrackUpdated = trackService.updateEntity(trackToUpdate);
        if(isTrackUpdated){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<?> deleteTrack(
            @PathVariable int trackId){

        var trackFromService = trackService.getEntityById(trackId);
        if(trackFromService.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var isTrackDeleted = trackService.deleteEntity(trackId);
        if(isTrackDeleted){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
