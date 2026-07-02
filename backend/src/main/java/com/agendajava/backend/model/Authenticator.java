package com.agendajava.backend.model;

import com.agendajava.backend.interfaces.Authenticable;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;

public class Authenticator implements Authenticable {
    public User login(String email, String password, DataManager dataManager) {
        User user = dataManager.findOne(
            dataManager.getUsersFile(), 
            User.class, 
            u -> u.getEmail().equals(email));
        
        if (user == null) {
            // print email ou senha errados (pode ser que o usuario n exista, mas eh melhor mostrar que os dados estao errados) 
            // pode ser outra exception
        } 

        return user; 
    }

    public User register(String name, String email, String password, String role, DataManager dataManager) {
        if (userExists(email, dataManager)) {
            // throw exception UserAlreadyExists e manda de volta para usuario escolher login
            return null;
        } 

        User user = null;

        // checagem do role do usuario para instanciar o objeto certo
        if (role.equalsIgnoreCase("doctor")) {
            user = new Doctor(name, email, password, null);
        } 
        if (role.equalsIgnoreCase("pacient")) {
            user = new Patient(name, email, password);
        }

        dataManager.add(dataManager.getUsersFile(), user);
        return user;
        
    }

    public boolean userExists(String email, DataManager dataManager) {
        User user = dataManager.findOne(
            dataManager.getUsersFile(), 
            User.class, 
            u -> u.getEmail().equals(email));

        if (user != null) {
            return true;
        } else {
            return false;
        }
    }
}
