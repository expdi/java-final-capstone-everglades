DROP TABLE IF EXISTS ARTIST CASCADE ;
DROP TABLE IF EXISTS TRACK CASCADE ;
DROP TABLE IF EXISTS TRACK_ARTIST CASCADE;

CREATE TABLE ARTIST (

    ID INTEGER GENERATED BY DEFAULT AS IDENTITY ,
    FIRST_NAME VARCHAR (255),
    LAST_NAME VARCHAR (255),

    PRIMARY KEY (ID)
);

CREATE TABLE TRACK(


    ID INTEGER GENERATED BY DEFAULT AS IDENTITY ,
    DURATION_IN_MINUTES FLOAT(53) NOT NULL ,
    ISSUE_DATE DATE,
    ALBUM VARCHAR(255),
    TITLE VARCHAR(255),
    TYPE VARCHAR(255) CHECK (TYPE IN ('OGG','MP3','FLAC','WAV')),

    PRIMARY KEY (ID)
);

CREATE TABLE TRACK_ARTIST(

    ARTIST_ID INTEGER NOT NULL ,
    TRACK_ID INTEGER NOT NULL ,

    PRIMARY KEY (ARTIST_ID, TRACK_ID),
    CONSTRAINT FK__ARTIST_ID FOREIGN KEY (ARTIST_ID) REFERENCES ARTIST,
    CONSTRAINT FK__TRACK_ID FOREIGN KEY (TRACK_ID) REFERENCES TRACK
);