package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventstoreDbClientConfiguration {
    // TODO - Move this configuration string in the application.properties file
    private static final String connectionString = "esdb://admin:changeit@esdb-local:2113?tls=false&tlsVerifyCert=false";

    @Bean
    public static EventStoreDBClient getClient() {
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBClient.create(settings);
    }
}
