package com.agendajava.backend.interfaces;

import com.agendajava.backend.model.DataManager;
import com.agendajava.backend.model.users.User;

public interface Authenticable {
    public User login(String email, String password, DataManager dataManager);

    public User register(String name, String email, String password, String role, DataManager dataManager);

    public boolean userExists(String email, DataManager dataManager);
}
