package com.agendajava.backend.interfaces;

import java.util.List;
import java.util.function.Predicate;

public interface Persistable {
    public <T> void save(String fileName, List<T> data);

    public <T> void add(String fileName, T object);

    public <T> void delete(String fileName, T object);

    public <T> void update(String fileName, T newObject, T oldObject);

    public <T> T findOne(String fileName, Class<T> type, Predicate<T> filter);
}
