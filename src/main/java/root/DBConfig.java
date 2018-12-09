package root;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.schema.Action;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

import javax.sql.DataSource;

@Profile("heroku")
@Configuration
@EnableJpaRepositories(basePackages = "root.repos", entityManagerFactoryRef = "emf")
public class DBConfig
{

    @Bean
    public DataSource configureDataSource()
    {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
        config.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
        config.setMaximumPoolSize(10);
        config.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
        DataSource dataSource = new HikariDataSource(config);


        return new HikariDataSource(config);
    }

    @Bean(name = "emf")
    public LocalContainerEntityManagerFactoryBean configureEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(configureDataSource());
        entityManagerFactoryBean.setPackagesToScan("root.entitys");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
        jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, Action.CREATE_DROP);
        jpaProperties.put(Environment.NON_CONTEXTUAL_LOB_CREATION, true);
        //spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }
}