package fr.grimtown.tools.utils.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.tools.Main;
import fr.grimtown.tools.utils.classes.Event;

public class EventsManager {
    private static final Datastore DATASTORE = Main.getDatastore("master");

    /**
     * Get an Event by his name
     */
    public static Event getEvent(String eventName) {
        return DATASTORE.find(Event.class)
                .filter(Filters.eq("name", eventName))
                .first();
    }
}
