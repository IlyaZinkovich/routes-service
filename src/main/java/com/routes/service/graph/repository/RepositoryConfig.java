package com.routes.service.graph.repository;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
public class RepositoryConfig extends Neo4jConfiguration {

    @Value("${neo4j.driver}")
    private String driver;

    @Value("${neo4j.user}")
    private String user;

    @Value("${neo4j.password}")
    private String password;

    @Value("${neo4j.uri}")
    private String uri;

    @Bean
    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        config
                .driverConfiguration()
                .setDriverClassName(driver)
                .setCredentials(user, password)
                .setURI(uri);
        return config;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(getConfiguration(), "com.routes.service.graph.model");
    }

    @Bean
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
