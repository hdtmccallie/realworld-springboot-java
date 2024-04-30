package io.github.raeperd.realworld.infrastructure.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@EnableJpaAuditing
@Configuration
class SpringDataJPAConfiguration  {


    @Bean
    public ResourceDatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.addScript(new ClassPathResource("data.sql"));
        // Add more scripts if necessary
        populator.setContinueOnError(false); // Continue if an error occurs?
        return populator;
    }

    @Bean
    public InitializingBean postInstall(ResourceDatabasePopulator populator, JdbcTemplate jdbc, DataSource dataSource){
        return () -> {
            var results = jdbc.query("SELECT name FROM sqlite_master WHERE type='table';", (rs, rowNum) -> rs.getString("name"));
            if( !results.contains("articles") ){
                populator.populate(dataSource.getConnection());
            }
        };
    }
}
