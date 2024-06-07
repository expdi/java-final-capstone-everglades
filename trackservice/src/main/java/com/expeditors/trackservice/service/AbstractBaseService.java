package com.expeditors.trackservice.service;

import com.expeditors.trackservice.domain.AbstractEntity;
import com.expeditors.trackservice.repository.BaseRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbstractBaseService<TEntity extends AbstractEntity>
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

        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("Null Entity Cannot be Added");
        }
        return repository.updateEntity(entity);
    }

    @Override
    public boolean deleteEntity(int id) {
        return repository.deleteEntity(id);
    }

    @Override
    public TEntity addEntity(TEntity entity) {

        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("Null Entity Cannot be Added");
        }
        return repository.addEntity(entity);
    }

    @Override
    public Optional<TEntity> getEntityById(int id) {
        return repository.getEntityById(id);
    }
}
