package com.expeditors.trackservice.config;

import com.expeditors.trackservice.domain.Artist;
import com.expeditors.trackservice.domain.Track;
import com.expeditors.trackservice.repository.taskmanagement.TaskManager;
import com.expeditors.trackservice.repository.taskmanagement.implementation.TaskManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Configuration
public class RepositoryConfig {

    @Bean
    public Map<Integer, Artist> getArtistMap(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Integer, Track> getTrackMap(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public TaskManager getTaskManager(){
        return new TaskManagerImpl(
                new ConcurrentLinkedDeque<>(),
                new ConcurrentLinkedDeque<>());
    }
}
