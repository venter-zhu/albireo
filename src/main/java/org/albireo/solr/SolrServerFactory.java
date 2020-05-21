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

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;

/**
 * Created by JunjieM on 2017-7-7.
 */
public class SolrServerFactory implements PooledObjectFactory<SolrClient> {

    private String[] servers;

    public SolrServerFactory(String solrServices, String collectionName) {
        String[] tempServers = solrServices.split(",");
        servers = new String[tempServers.length];
        for (int i = 0; i < tempServers.length; i++) {
            servers[i] = "http://" + tempServers[i] + "/solr/" + collectionName;
        }
    }

    public PooledObject<SolrClient> makeObject() throws Exception {
        SolrClient solrClient = new LBHttpSolrClient.Builder().withBaseSolrUrls(servers).build();
        return new DefaultPooledObject(solrClient);
    }

    public void destroyObject(PooledObject<SolrClient> pool) throws Exception {
        SolrClient solrClient = pool.getObject();
        if (solrClient != null) {
            solrClient.close();
            solrClient = null;
        }
    }

    public void activateObject(PooledObject<SolrClient> pool) throws Exception {
        // TODO Auto-generated method stub
    }

    public void passivateObject(PooledObject<SolrClient> pool) throws Exception {
        // TODO Auto-generated method stub
    }

    public boolean validateObject(PooledObject<SolrClient> pool) {
        // TODO Auto-generated method stub
        return false;
    }

}