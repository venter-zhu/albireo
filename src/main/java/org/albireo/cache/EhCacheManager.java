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

package org.albireo.cache;

import org.ehcache.Cache;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.URL;
import java.util.Date;

/**
 * Created by yfyuan on 2017/2/4.
 */
public class EhCacheManager<T> implements CacheManager<T>, InitializingBean, DisposableBean {

    private static org.ehcache.CacheManager myCacheManager;

    private Cache<String, CacheObject> cache;

    private String cacheAlias;

    static {
        final URL myUrl = EhCacheManager.class.getResource("/ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
    }

    @Override
    public void put(String key, T data, long expire) {
        cache.put(key, new CacheObject(new Date().getTime(), expire, data));
    }

    @Override
    public T get(String key) {
        CacheObject o = cache.get(key);
        if (o == null || new Date().getTime() >= o.getT1() + o.getExpire())
            return null;
        else {
            return (T) o.getD();
        }
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    public void setCacheAlias(String cacheAlias) {
        this.cacheAlias = cacheAlias;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = myCacheManager.getCache(cacheAlias, String.class, CacheObject.class);
    }

    @Override
    public void destroy() throws Exception {
        EhCacheManager.myCacheManager.close();
    }
}
