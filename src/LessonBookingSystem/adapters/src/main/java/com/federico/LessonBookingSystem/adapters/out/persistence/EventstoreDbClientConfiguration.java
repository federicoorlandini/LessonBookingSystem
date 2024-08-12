package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventstoreDbClientConfiguration {
    // TODO - Move this configuration string in the application.properties file
    private static final String connectionString = "esdb://admin:changeit@localhost:2113?tls=false&tlsVerifyCert=false";
    private static final EventStoreDBClientSettings settings = EventStoreDBConnectionString.parseOrThrow(connectionString);

    @Bean
    public static EventStoreDBClient getClient() {
        return EventStoreDBClient.create(settings);
    }

    @Bean
    public static EventStoreDBProjectionManagementClient getProjectionClient() {
        return EventStoreDBProjectionManagementClient.create(settings);
    }

}
