package com.agendajava.backend.model;

import com.agendajava.backend.interfaces.Persistable;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DataManager implements Persistable {
    private final String USERS_FILE = "users.json";
    private final String PROCEDURES_FILE = "procedures.json";
    private final String ROOMS_FILE = "rooms.json";
    private final ObjectMapper objectMapper;

    public DataManager() {
        this.objectMapper = new ObjectMapper();
    }

    public String getUsersFile() {
        return this.USERS_FILE;
    }

    public String getProceduresFile() {
        return this.PROCEDURES_FILE;
    }

    public String getRoomsFile() {
        return this.ROOMS_FILE;
    }

    private <T> List<T> jsonToList(String fileName, Class<T> type) {
        File file = new File(fileName);

        // se o json estiver vazio, retornamos uma lista vazia
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        // se o json tiver informações, transformamos ele em uma lista para auxiliar nas operações
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
        return objectMapper.readValue(file, listType);
    }

    public <T> void save(String fileName, List<T> data) {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), data);
    }

    public <T> void add(String fileName, T object) {
        List<T> list = jsonToList(fileName, (Class<T>) object.getClass());
        list.add(object);
        save(fileName, list);
    }

    public <T> void delete(String fileName, T object) {
        List<T> list = jsonToList(fileName, (Class<T>) object.getClass());

        // itera sobre a lista e compara todos os objetos ao object, quando retornar true, remove ele da lista
        list.removeIf(listItem -> {
            try {
                String itemJson = objectMapper.writeValueAsString(listItem);
                String objectJson = objectMapper.writeValueAsString(object);

                return itemJson.equals(objectJson);
            } catch (Exception e) {
                return false;
            }
        });
        save(fileName, list);
    }

    // método de update! 
    public <T> void update(String fileName, T updatedObject, Predicate<T> filter) {
        List<T> list = jsonToList(fileName, (Class<T>) updatedObject.getClass());

        for (int i = 0; i < list.size(); i++) {
            if (filter.test(list.get(i))) {
                // 3. Achou! Substitui o objeto antigo pelo atualizado
                list.set(i, updatedObject);
                break; // Para o loop
            }
        }
        
        save(fileName, list);
    }

    public <T> T findOne(String fileName, Class<T> type, Predicate<T> filter) {
        List<T> list = jsonToList(fileName, type);

        return list.stream().filter(filter).findFirst().orElse(null);

        // exemplo do predicate: user -> user.getEmail().equals(emailDigitado)
    }

}
