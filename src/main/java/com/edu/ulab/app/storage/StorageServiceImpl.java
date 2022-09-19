package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Entity;
import com.edu.ulab.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class StorageServiceImpl implements StorageService {

    private final Storage storage;

    public StorageServiceImpl(String storageName) {
        this.storage = new Storage(storageName);
    }

    @Override
    public Entity save(Entity entity) {
        HashMap<Long, Entity> rows = storage.getRows();
        if (entity.getId() == null) {
            entity.setId(storage.getID());
            storage.setID(entity.getId() + 1);
            rows.put(entity.getId(), entity);
            log.info("Saved new entity 'id:{}' in {}", entity.getId(), storage.getName());
        } else if (rows.containsKey(entity.getId())) {
            rows.put(entity.getId(), entity);
        }
        return entity;
    }

    @Override
    public Entity findById(Long id) {
        HashMap<Long, Entity> rows = storage.getRows();
        if (!rows.containsKey(id)) {
            throw new NotFoundException("User does not exist");
        }
        return rows.get(id);
    }

    @Override
    public Entity deleteById(Long id) {
        HashMap<Long, Entity> rows = storage.getRows();
        if (!rows.containsKey(id)) {
            throw new NotFoundException("User does not exist");
        }
        Entity remove = rows.remove(id);
        log.info("Removed entity 'id:{}' from {}", id, storage.getName());
        return remove;
    }

    @Override
    public List<Entity> getAll() {
        HashMap<Long, Entity> rows = storage.getRows();
        return rows.values().stream().toList();
    }
}
