package fr.grimtown.tools.utils.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.tools.Main;
import fr.grimtown.tools.utils.classes.Profile;

public class ProfileManager {
    private static final Datastore DATASTORE = Main.getDatastore("master");

    /**
     * Get a Profile by his UUID
     */
    public static Profile getProfile(String username) {
        return DATASTORE.find(Profile.class)
                .filter(Filters.eq("username", username))
                .first();
    }
}
