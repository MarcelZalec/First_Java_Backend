package org.example.push;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {
    private List<PushSubscription> subs = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/resources/subscriptions.json");

    public SubscriptionService() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("subscriptions.json");
            subs = mapper.readValue(is, new TypeReference<List<PushSubscription>>() {});
        } catch (Exception ignored) {}
    }

    public void add(PushSubscription sub) {
        subs.add(sub);
        save();
    }

    public List<PushSubscription> getAll() {
        return subs;
    }

    private void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, subs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

