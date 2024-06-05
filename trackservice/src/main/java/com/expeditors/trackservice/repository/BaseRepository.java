package com.expeditors.trackservice.repository;

import com.expeditors.trackservice.domain.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<TEntity extends AbstractEntity> {

    List<TEntity> getAllEntities();
    boolean updateEntity(TEntity entity);
    boolean deleteEntity(int id);
    TEntity addEntity(TEntity entity);
    Optional<TEntity> getEntityById(int id);
}
