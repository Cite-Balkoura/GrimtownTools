package fr.grimtown.tools.utils.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.util.UUID;

@Entity(value = "profile")
public class Profile {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username;
    @Indexed(options = @IndexOptions(unique = true))
    private UUID uuid;

    public Profile() {}

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }
}
