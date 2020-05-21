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

package org.albireo.solr;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by JunjieM on 2017-7-7.
 */
public class SolrServerPoolFactory {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private GenericObjectPool<SolrClient> pool;

    public SolrServerPoolFactory(GenericObjectPoolConfig config, String solrServices, String collectionName) {
        SolrServerFactory factory = new SolrServerFactory(solrServices, collectionName);
        pool = new GenericObjectPool(factory, config);
    }

    public SolrClient getConnection() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            LOG.error("", e);
            return null;
        }
    }

    public void releaseConnection(SolrClient solrClient) {
        try {
            pool.returnObject(solrClient);
        } catch (Exception e) {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (IOException e1) {
                    LOG.error("", e1);
                }
                solrClient = null;
            }
            LOG.error("", e);
        }
    }

    public void closePool() {
        if (pool != null) {
            try {
                pool.close();
                pool = null;
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }
}
