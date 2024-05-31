package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Entity;

import java.util.List;
import java.util.Optional;

public interface BaseService<TEntity extends Entity> {

    List<TEntity> getAllEntities();
    boolean updateEntity(TEntity entity);
    boolean deleteEntity(int id);
    TEntity addEntity(TEntity entity);
    Optional<TEntity> getEntityById(int id);

}
