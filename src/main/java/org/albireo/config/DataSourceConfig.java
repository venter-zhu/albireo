/*
 * Copyright Albireo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.albireo.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 多数据源配置
 *
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/22 0022 23:19
 **/
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.dbcp2.url}")
    private String url;
    @Value("${spring.datasource.dbcp2.password}")
    private String password;
    @Value("${spring.datasource.dbcp2.username}")
    private String username;
    @Value("${spring.datasource.dbcp2.driver-class-name}")
    private String driverClassName;

    @Bean(destroyMethod = "close",name = "h2DataSource")
    public BasicDataSource getDataSource(){
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(url);
        basicDataSource.setPassword(password);
        basicDataSource.setUsername(username);
        basicDataSource.setDriverClassName(driverClassName);
        basicDataSource.setMaxTotal(20);
        return basicDataSource;
    }

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.meta")
    public DataSource getDateSource()
    {
        return DataSourceBuilder.create().build();
    }
}
