package com.expeditors.trackservice.dto;

import com.expeditors.trackservice.domain.Artist;
import lombok.*;

import java.util.HashSet;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistRequest {

    private String firstName;
    private String lastName;

    public Artist toArtist(){

        return Artist.builder()
                .firstName(firstName)
                .lastName(lastName)
                .trackList(new HashSet<>())
                .build();
    }
}
