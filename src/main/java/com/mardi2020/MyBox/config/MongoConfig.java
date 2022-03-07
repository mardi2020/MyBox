package com.mardi2020.MyBox.config;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.mardi2020.MyBox", mongoTemplateRef = "myBoxMongoTemplate")

@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public MongoTemplate myBoxMongoTemplate(MongoClient mongoClient) {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, "myBox");
        return new MongoTemplate(factory);
    }
}
