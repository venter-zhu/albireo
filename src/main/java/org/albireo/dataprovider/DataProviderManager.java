/*
 * Copyright Cboard
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

package org.albireo.dataprovider;

import org.albireo.dataprovider.aggregator.InnerAggregator;
import org.albireo.dataprovider.aggregator.h2.H2Aggregator;
import org.albireo.dataprovider.annotation.DatasourceParameter;
import org.albireo.dataprovider.annotation.ProviderName;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Service
public class DataProviderManager implements ApplicationContextAware {

    private static Logger LOG = LoggerFactory.getLogger(DataProviderManager.class);

    private static Map<String, Class<? extends DataProvider>> providers = new HashMap<>();

    private static ApplicationContext applicationContext;

    static {
        Set<Class<?>> classSet = new Reflections("org.albireo").getTypesAnnotatedWith(ProviderName.class);
        for (Class c : classSet) {
            if (DataProvider.class.isAssignableFrom(c)) {
                providers.put(((ProviderName) c.getAnnotation(ProviderName.class)).name(), c);
            } else {
                System.out.println("自定义DataProvider需要继承org.albireo.dataprovider.DataProvider");
            }
        }
    }

    public static Set<String> getProviderList() {
        return providers.keySet();
    }

    /*public static DataProvider getDataProvider(String type) throws Exception {
        return getDataProvider(type, null, null);
    }*/

    public static DataProvider getDataProvider(
            String type, Map<String, String> dataSource,
            Map<String, String> query) throws Exception {
        return getDataProvider(type, dataSource, query, false);
    }

    public static DataProvider getDataProvider(
            String type, Map<String, String> dataSource,
            Map<String, String> query,
            boolean isUseForTest) throws Exception {
        Class c = providers.get(type);
        ProviderName providerName = (ProviderName) c.getAnnotation(ProviderName.class);
        if (providerName.name().equals(type)) {
            DataProvider provider = (DataProvider) c.newInstance();
            provider.setQuery(query);
            provider.setDataSource(dataSource);
            provider.setUsedForTest(isUseForTest);
            if (provider instanceof Initializing) {
                ((Initializing) provider).afterPropertiesSet();
            }
            applicationContext.getAutowireCapableBeanFactory().autowireBean(provider);
            InnerAggregator innerAggregator = applicationContext.getBean(H2Aggregator.class);
            innerAggregator.setDataSource(dataSource);
            innerAggregator.setQuery(query);
            provider.setInnerAggregator(innerAggregator);
            return provider;
        }
        return null;
    }

    protected static Class<? extends DataProvider> getDataProviderClass(String type) {
        return providers.get(type);
    }

    public static List<String> getProviderFieldByType(String providerType, DatasourceParameter.Type type) throws IllegalAccessException, InstantiationException {
        Class clz = getDataProviderClass(providerType);
        Object o = clz.newInstance();
        Set<Field> fieldSet = ReflectionUtils.getAllFields(clz, ReflectionUtils.withAnnotation(DatasourceParameter.class));
        return fieldSet.stream().filter(e ->
                e.getAnnotation(DatasourceParameter.class).type().equals(type)
        ).map(e -> {
            try {
                e.setAccessible(true);
                return e.get(o).toString();
            } catch (IllegalAccessException e1) {
                LOG.error("" , e);
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
