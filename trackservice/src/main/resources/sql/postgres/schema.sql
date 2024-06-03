drop table if exists artist cascade;
drop table if exists track cascade;
drop table if exists track_artist_list cascade;

create table artist (
                        id integer generated by default as identity,
                        first_name varchar(255),
                        last_name varchar(255),
                        primary key (id)
);

create table track (
                       duration_in_minutes float(53) not null,
                       id integer generated by default as identity,
                       issue_date date,
                       album varchar(255),
                       title varchar(255),
                       type varchar(255) check (type in ('OGG','MP3','FLAC','WAV')),
                       primary key (id)
);

create table track_artist_list (
                                   artist_list_id integer not null,
                                   track_list_id integer not null,
                                   primary key (artist_list_id, track_list_id)
);
    
alter table if exists track_artist_list
    add constraint FK_artist_list_id
        foreign key (artist_list_id)
            references artist;
    
alter table if exists track_artist_list
    add constraint FK_track_list_id
        foreign key (track_list_id)
            references track;