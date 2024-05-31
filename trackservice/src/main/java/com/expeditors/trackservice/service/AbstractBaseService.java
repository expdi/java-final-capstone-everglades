package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.Entity;
import com.expeditors.trackservice.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public class AbstractBaseService<TEntity extends Entity>
        implements BaseService<TEntity> {

    private final BaseRepository<TEntity> repository;

    public AbstractBaseService(BaseRepository<TEntity> repository) {
        this.repository = repository;
    }

    @Override
    public List<TEntity> getAllEntities() {
        return repository.getAllEntities();
    }

    @Override
    public boolean updateEntity(TEntity entity) {
        return repository.updateEntity(entity);
    }

    @Override
    public boolean deleteEntity(int id) {
        return repository.deleteEntity(id);
    }

    @Override
    public TEntity addEntity(TEntity entity) {
        return repository.addEntity(entity);
    }

    @Override
    public Optional<TEntity> getEntityById(int id) {
        return repository.getEntityById(id);
    }
}
