package com.agendajava.backend.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.agendajava.backend.interfaces.Persistable;
import com.agendajava.backend.model.procedures.Procedure;
import com.agendajava.backend.model.rooms.Room;
import com.agendajava.backend.model.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class DataManager implements Persistable {
    private final String USERS_FILE = "users.json";
    private final String PROCEDURES_FILE = "procedures.json";
    private final String ROOMS_FILE = "rooms.json";
    private final ObjectMapper objectMapper;

    public DataManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public String getUsersFile() { return this.USERS_FILE; }
    public String getProceduresFile() { return this.PROCEDURES_FILE; }
    public String getRoomsFile() { return this.ROOMS_FILE; }

    private Class<?> getBaseClassForFile(String fileName) {
        if (fileName.equals(USERS_FILE)) return User.class;
        if (fileName.equals(PROCEDURES_FILE)) return Procedure.class;
        if (fileName.equals(ROOMS_FILE)) return Room.class;
        return Object.class;
    }

    private <T> List<T> jsonToList(String fileName, Class<T> type) {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
        try {
            return objectMapper.readValue(file, listType);
        } catch (Exception e) {
            System.err.println("======== ERRO CRÍTICO ========");
            System.err.println("Falha ao ler o arquivo: " + fileName);
            e.printStackTrace();
            System.err.println("==============================");
            throw new RuntimeException("Falha na leitura do JSON para evitar perda de dados.", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void save(String fileName, List<T> data) {
        Class<?> baseClass = getBaseClassForFile(fileName);
        save(fileName, data, (Class<T>) baseClass);
    }

    public <T> void save(String fileName, List<T> data, Class<T> baseClass) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, baseClass);
            objectMapper.writerFor(listType).withDefaultPrettyPrinter().writeValue(new File(fileName), data);
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo: " + fileName);
            e.printStackTrace();
        }
    }

    @Override
    public <T> void add(String fileName, T object) {
        Class<?> baseClass = getBaseClassForFile(fileName);
        addInternal(fileName, object, baseClass);
    }

    @SuppressWarnings("unchecked")
    private <T, B> void addInternal(String fileName, T object, Class<B> baseClass) {
        List<B> list = jsonToList(fileName, baseClass);
        list.add((B) object);
        save(fileName, list, baseClass);
    }

    @Override
    public <T> void delete(String fileName, T object) {
        Class<?> baseClass = getBaseClassForFile(fileName);
        deleteInternal(fileName, object, baseClass);
    }

    private <T, B> void deleteInternal(String fileName, T object, Class<B> baseClass) {
        List<B> list = jsonToList(fileName, baseClass);
        list.removeIf(listItem -> {
            try {
                String itemJson = objectMapper.writeValueAsString(listItem);
                String objectJson = objectMapper.writeValueAsString(object);
                return itemJson.equals(objectJson);
            } catch (Exception e) {
                return false;
            }
        });
        save(fileName, list, baseClass);
    }

    @Override
    public <T> void update(String fileName, T updatedObject, Predicate<T> filter) {
        Class<?> baseClass = getBaseClassForFile(fileName);
        updateInternal(fileName, updatedObject, filter, baseClass);
    }

    @SuppressWarnings("unchecked")
    private <T, B> void updateInternal(String fileName, T updatedObject, Predicate<T> filter, Class<B> baseClass) {
        List<B> list = jsonToList(fileName, baseClass);
        
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            B item = list.get(i);
            
            if (updatedObject.getClass().isInstance(item)) {
                T castedItem = (T) item;
                if (filter.test(castedItem)) {
                    list.set(i, (B) updatedObject);
                    found = true;
                    break; 
                }
            }
        }
        
        if (found) {
            save(fileName, list, baseClass);
        }
    }

    @Override
    public <T> T findOne(String fileName, Class<T> type, Predicate<T> filter) {
        List<T> list = jsonToList(fileName, type);
        return list.stream().filter(filter).findFirst().orElse(null);
    }

    @Override
    public <T> List<T> findAll(String fileName, Class<T> type) {
        return jsonToList(fileName, type);
    }
}