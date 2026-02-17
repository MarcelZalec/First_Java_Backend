package org.example.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

@Service
public class UserService {

    private List<CustomUser> users = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File userFile = new File("src/main/resources/users.json");

    public UserService() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("users.json");
            users = mapper.readValue(is, new TypeReference<List<CustomUser>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Laden der users.json", e);
        }
    }

    public CustomUser findUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean addUser(CustomUser user) {
        if (findUser(user.getUsername()) != null) {
            return false;
        }

        users.add(user);
        saveUsers();
        return true;
    }

    private void saveUsers() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(userFile, users);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Speichern der users.json", e);
        }
    }
}
