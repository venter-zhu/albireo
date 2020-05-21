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

package org.albireo.dataprovider.aggregator.h2.job;

import org.apache.commons.dbcp2.BasicDataSource;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by zyong on 2017/9/18.
 */
@Component
public class CleanerService implements InitializingBean {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    @Qualifier("h2DataSource")
    private BasicDataSource jdbcDataSource;

    @Value("${aggregator.h2.database.name}")
    private String h2DbName;

    @Value("${aggregator.h2.cleanjob.quarz}")
    private String quartz;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("========================Initialize H2 DataBase CleanerJob=================================");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(CleanJobExecutor.class).build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartz))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }


    protected void cleanDB() {
        try (Connection conn = jdbcDataSource.getConnection();
             Statement statmt = conn.createStatement();) {
            String resetDB = "DROP ALL OBJECTS DELETE FILES";
            LOG.info("Execute: {}", resetDB);
            statmt.execute(resetDB);
        } catch (SQLException e) {
            LOG.error("", e);
        }
    }

}
