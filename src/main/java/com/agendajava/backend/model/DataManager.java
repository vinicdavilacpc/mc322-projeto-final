package com.agendajava.backend.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.agendajava.backend.interfaces.Persistable;
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
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
        
        try {
            return objectMapper.readValue(file, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <T> void save(String fileName, List<T> data) {
        // ADICIONADO TRY-CATCH
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), data);
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo: " + fileName);
            e.printStackTrace();
        }
    }

    @Override
    public <T> void add(String fileName, T object) {
        List<T> list = jsonToList(fileName, (Class<T>) object.getClass());
        list.add(object);
        save(fileName, list);
    }

    @Override
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

    @Override
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

    @Override
    public <T> T findOne(String fileName, Class<T> type, Predicate<T> filter) {
        List<T> list = jsonToList(fileName, type);

        return list.stream().filter(filter).findFirst().orElse(null);

        // exemplo do predicate: user -> user.getEmail().equals(emailDigitado)
    }

}
