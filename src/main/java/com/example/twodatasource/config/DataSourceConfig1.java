package com.example.twodatasource.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.twodatasource.test1.mapper", sqlSessionFactoryRef = "test1SqlSessionFactory")
public class DataSourceConfig1 {
    @Bean(name = "test1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DataSource getDataSource1() {
        return DataSourceBuilder.create().build();
    }

//    @Bean(name = "test1SqlSessionFactory")
//    @Primary
//    public SqlSessionFactory test1SqlSessionFactory(@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/test1/*.xml"));
//        return bean.getObject();
//    }

    @Bean(name = "test1SqlSessionFactory")
    public SqlSessionFactory test1SqlSessionFactory(@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/test/*.xml"));
        return bean.getObject();
    }
    @Bean(name = "test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate test1sqlsessiontemplate(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
