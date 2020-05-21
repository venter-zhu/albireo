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

package org.albireo.services.job;

import org.albireo.pojo.DashboardJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by yfyuan on 2017/2/20.
 */
public class MailJobExecutor implements Job {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobService jobService = ((ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext")).getBean(JobService.class);
            jobService.sendMail((DashboardJob) jobExecutionContext.getMergedJobDataMap().get("job"));
        } catch (SchedulerException e) {
            LOG.error("", e);
        }
    }
}
