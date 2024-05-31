package com.expeditors.trackservice.repository.implementations;

import com.expeditors.trackservice.domain.Entity;
import com.expeditors.trackservice.repository.BaseRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractBaseInMemoryRepository<TEntity extends Entity>
        implements BaseRepository<TEntity> {

    private final Map<Integer, TEntity> entityList;
    private static final AtomicInteger lastId = new AtomicInteger();

    public AbstractBaseInMemoryRepository(
            Map<Integer, TEntity> entityList) {

        this.entityList = entityList;
    }

    @Override
    public List<TEntity> getAllEntities() {
        return new ArrayList<>(entityList.values());
    }

    @Override
    public boolean updateEntity(TEntity entity) {
        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("Entity cannot be null");
        }

        return entityList.computeIfPresent(entity.getId(),
                (key, oldValue) -> entity) != null;
    }

    @Override
    public boolean deleteEntity(int id) {
        return entityList.remove(id) != null;
    }

    @Override
    public TEntity addEntity(TEntity entity) {
        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("Id cannot be null");
        }

        int newId = lastId.incrementAndGet();
        entity.setId(newId);
        entityList.put(newId, entity);

        return entity;
    }

    @Override
    public Optional<TEntity> getEntityById(int id) {
        if(entityList.containsKey(id)){
            return Optional.of(entityList.get(id));
        }

        return Optional.empty();
    }
}
