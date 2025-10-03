package gov.govcircle.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;



@Configuration
@EnableTransactionManagement
@EntityScan(
        basePackages = {
                // Application
                "gov.govcircle.common.security.model.entity",

                // Constitution
                "gov.govcircle.constitution.constitution.model.entities",

                // Opinion
                "gov.govcircle.constitution.opinion.model.entities",

                // Rule
                "gov.govcircle.constitution.rule.model.entities"
        }
)
@EnableJpaRepositories(
        basePackages = {
                // Application
                "gov.govcircle.common.user.repository",
                "gov.govcircle.common.security.repository",

                // Constitution
                "gov.govcircle.constitution.constitution.repository",

                // Opinion
                "gov.govcircle.constitution.opinion.repository",

                // Rule
                "gov.govcircle.constitution.rule.repository"
        },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "govCircleTransactionManager"

)
public class GovCircleDataSourceConfiguration {

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("govCircleDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan(
                // Application
                "gov.govcircle.common.security.model.entity",

                // Constitution
                "gov.govcircle.constitution.constitution.model.entities",

                // Opinion
                "gov.govcircle.constitution.opinion.model.entities",

                // Rule
                "gov.govcircle.constitution.rule.model.entities"
        );
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);

        Map<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create");
        bean.setJpaPropertyMap(props);

        return bean;

    }

    @Primary
    @Bean(name = "govCircleTransactionManager")
    public PlatformTransactionManager govCircleTransactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean govCircleEntityManagerFactory
    ) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(govCircleEntityManagerFactory.getObject());
        return manager;

    }

    @Primary
    @Bean(name = "govCircleDataSource")
    public DataSource govCircleDataSource() {
        return govCircleDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();

    }

    @Primary
    @Bean(name = "govCircleDataSourceProperties")
    @ConfigurationProperties("spring.datasource.gov-circle")
    public DataSourceProperties govCircleDataSourceProperties() {
        return new DataSourceProperties();

    }

}
