package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Entity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StorageService {
    Entity save(Entity entity);

    Entity findById(Long id);

    Entity deleteById(Long id);

    List<Entity> getAll();
}
