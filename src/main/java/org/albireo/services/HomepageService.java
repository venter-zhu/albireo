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

package org.albireo.services;

import org.albireo.dao.HomepageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by february on 2018/12/20.
 */
@Service
public class HomepageService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private HomepageDao homepageDao;
    
    public ServiceStatus resetHomepage(String userId) {
        try {
            homepageDao.resetHomepage(userId);
            return new ServiceStatus(ServiceStatus.Status.Success, "success");
        } catch (Exception e) {
            LOG.error("", e);
            return new ServiceStatus(ServiceStatus.Status.Fail, e.getMessage());
        }
    }
    
    public Long selectHomepage(String userId) {
        Long boardId = homepageDao.selectHomepage(userId);
        return boardId;
    }
    
    public ServiceStatus saveHomepage(Long boardId, String userId) {
        try {
            homepageDao.resetHomepage(userId);
            homepageDao.saveHomepage(boardId, userId);
            return new ServiceStatus(ServiceStatus.Status.Success, "success");
        } catch (Exception e) {
            LOG.error("", e);
            return new ServiceStatus(ServiceStatus.Status.Fail, e.getMessage());
        }
    }

}
