package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Entity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Data
public class Storage {
    //todo
    // создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции
    private final String name;
    private Long ID;
    private HashMap<Long, Entity> rows = new HashMap<>();


    public Storage(String name) {
        this.name = name;
        this.ID = 1L;
        log.info("Created storage: {}", name);
    }

}
