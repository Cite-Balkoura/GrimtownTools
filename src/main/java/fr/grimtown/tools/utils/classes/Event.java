package fr.grimtown.tools.utils.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity(value = "event")
public class Event {
    @Id
    private ObjectId id;
    private String name;
    private String database;

    public Event() {}

    public String getName() {
        return name;
    }

    public String getDatabase() {
        return database;
    }
}