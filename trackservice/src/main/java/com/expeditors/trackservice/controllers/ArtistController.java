package com.expeditors.trackservice.controllers;

import com.expeditors.trackservice.dto.ArtistRequest;
import com.expeditors.trackservice.dto.ArtistResponse;
import com.expeditors.trackservice.dto.TrackResponse;
import com.expeditors.trackservice.service.ArtistService;
import com.expeditors.trackservice.service.PricingProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;
    private final PricingProvider pricingProvider;

    public ArtistController(
            ArtistService artistService,
            PricingProvider pricingProvider) {

        this.artistService = artistService;
        this.pricingProvider = pricingProvider;
    }

    @GetMapping
    public ResponseEntity<?> getAllArtists(){

        return ResponseEntity
                .ok()
                .body(artistService.getAllEntities()
                        .stream()
                        .map(a -> ArtistResponse.fromArtist(a, pricingProvider.getPrice()))
                        .toList());
    }

    @GetMapping("/by/id/{artistId}")
    public ResponseEntity<?> getArtistById(
            @PathVariable int artistId){

        return artistService.getEntityById(artistId)
                .map(
                        a -> ResponseEntity
                                .ok()
                                .body(ArtistResponse.fromArtist(a, pricingProvider.getPrice())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by/name/{artistName}")
    public ResponseEntity<?> getArtistByName(
            @PathVariable String artistName){

        return ResponseEntity
                .ok()
                .body(
                        artistService.getArtistByName(artistName)
                                .stream()
                                .map(a -> ArtistResponse.fromArtist(a, pricingProvider.getPrice()))
                                .toList()
                );

    }

    @GetMapping("/{artistId}/tracks")
    public ResponseEntity<?> getTrackForArtist(
            @PathVariable int artistId ){

        return ResponseEntity
                .ok()
                .body(
                        artistService.getTracksByArtistId(artistId)
                                .stream()
                                .map(t -> TrackResponse.fromTrack(t, pricingProvider.getPrice()))
                                .toList()
                );
    }


    @PostMapping
    public ResponseEntity<?> addArtist(
            @Valid @RequestBody ArtistRequest artistRequest){

        var artistCreated = artistService.addEntity(artistRequest.toArtist());
        return ResponseEntity
                .ok()
                .body(ArtistResponse.fromArtist(artistCreated, pricingProvider.getPrice()));

    }

    @PutMapping("/{artistId}")
    public ResponseEntity<?> updateArtist(
            @PathVariable int artistId,
            @RequestBody ArtistRequest artistRequest){

        var artistFromService = artistService.getEntityById(artistId);

        if(artistFromService.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var artistToUpdate = artistFromService.get();
        artistToUpdate.setFirstName(artistRequest.getFirstName());
        artistToUpdate.setLastName(artistRequest.getLastName());

        if(artistService.updateEntity(artistToUpdate)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<?> deleteArtist(
            @PathVariable int artistId){

        var artistFromService = artistService.getEntityById(artistId);

        if(artistFromService.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(artistService.deleteEntity(artistId)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.internalServerError().build();
    }


}
