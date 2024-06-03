package com.expeditors.trackservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Set;
@Entity
public class Artist extends AbstractEntity {

    private String firstName;
    private String lastName;
    @JsonIgnore
    @ManyToMany( mappedBy = "artistList" )
    private Set<Track> trackList;


    public Artist() {
    }

    public Artist(
            String firstName,
            String lastName,
            Set<Track> trackList) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.trackList = trackList;
    }

    public Artist(
            int id,
            String firstName,
            String lastName,
            Set<Track> trackList) {

        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.trackList = trackList;
    }



    public static ArtistBuilder builder(){
        return new ArtistBuilder();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(Set<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return  Objects.equals(getId(), artist.getId()) &&
                Objects.equals(firstName, artist.firstName) &&
                Objects.equals(lastName, artist.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", trackList=" + trackList +
                ", id=" + id +
                '}';
    }
}
