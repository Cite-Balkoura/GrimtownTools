package fr.grimtown.tools.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.utils.classes.Event;
import fr.grimtown.tools.utils.classes.Profile;
import org.bson.UuidRepresentation;
import org.bukkit.configuration.Configuration;

import java.util.Collections;
import java.util.HashMap;

public class MongoDB {
    /**
     * Load DataStores
     */
    public static HashMap<String, Datastore> getDatastoreMap(Configuration config) {
        HashMap<String, Datastore> datastoreMap = new HashMap<>();
        for (Object dbName : config.getStringList("data.mongo.databases")) {
            datastoreMap.put(dbName.toString(), setDatastore(config, dbName.toString()).startSession());
        }
        return datastoreMap;
    }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    private static Datastore setDatastore(Configuration config, String dbName) {
        MongoCredential credential = MongoCredential.createCredential(
                config.getString("data.mongo.user"),
                config.getString("data.mongo.db"),
                config.getString("data.mongo.password").toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(config.getString("data.mongo.host"), config.getInt("data.mongo.port")))))
                .credential(credential)
                .build();
        Datastore datastore = Morphia.createDatastore(MongoClients.create(settings), dbName, MapperOptions.builder()
                .enablePolymorphicQueries(true)
                .build());
        if (dbName.equalsIgnoreCase("master")) datastore.getMapper().map(Profile.class, Event.class);
        else datastore.getMapper().map(ConnectionSave.class, DeathSave.class);
        datastore.ensureIndexes();
        datastore.ensureCaps();
        datastore.enableDocumentValidation();
        return datastore;
    }
}
