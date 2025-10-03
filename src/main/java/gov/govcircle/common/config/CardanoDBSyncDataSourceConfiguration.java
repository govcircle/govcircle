//package gov.govcircle.common.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = {
//                "gov.govcircle.common.indexer.dbsync.repository"
//        },
//        entityManagerFactoryRef = "cardanoDBSyncEntityManagerFactory",
//        transactionManagerRef = "cardanoDBSyncTransactionManager"
//)
//public class CardanoDBSyncDataSourceConfiguration {
//
//
//    @Bean(name = "cardanoDBSyncEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean cardanoDBSyncEntityManagerFactory(
//            @Qualifier("cardanoDBSyncDataSource") DataSource dataSource
//    ) {
//        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setPackagesToScan(
//                "gov.govcircle.common.indexer.dbsync.model.entity"
//        );
//
//        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        bean.setJpaVendorAdapter(adapter);
//
//        Map<String, String> props = new HashMap<>();
//        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        props.put("hibernate.show_sql", "true");
//        props.put("hibernate.hbm2ddl.auto", "none");
//        bean.setJpaPropertyMap(props);
//
//        return bean;
//    }
//
//    @Bean(name = "cardanoDBSyncTransactionManager")
//    public PlatformTransactionManager cardanoDBSyncTransactionManager(
//            @Qualifier("cardanoDBSyncEntityManagerFactory") LocalContainerEntityManagerFactoryBean cardanoDBSyncEntityManagerFactory
//    ) {
//        JpaTransactionManager manager = new JpaTransactionManager();
//        manager.setEntityManagerFactory(cardanoDBSyncEntityManagerFactory.getObject());
//        return manager;
//
//    }
//
//    @Bean(name = "cardanoDBSyncDataSource")
//    public DataSource cardanoDBSyncDataSource() {
//        return cardanoDBSyncDataSourceProperties()
//                .initializeDataSourceBuilder()
//                .build();
//
//    }
//
//    @Bean(name = "cardanoDBSyncDataSourceProperties")
//    @ConfigurationProperties("spring.datasource.cardano-db-sync")
//    public DataSourceProperties cardanoDBSyncDataSourceProperties() {
//        return new DataSourceProperties();
//
//    }
//
//}
