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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.albireo.dao.JobDao;
import org.albireo.dto.ViewDashboardJob;
import org.albireo.pojo.DashboardJob;
import org.albireo.services.MailService;
import org.albireo.services.ServiceStatus;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by yfyuan on 2017/2/17.
 */
@Component
public class JobService implements InitializingBean {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final JobDao jobDao;

    @Value("${admin_user_id}")
    private String adminUserId;

    private final MailService mailService;

    private static Logger LOG = LoggerFactory.getLogger(JobService.class);

    public JobService(SchedulerFactoryBean schedulerFactoryBean, JobDao jobDao, MailService mailService) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.jobDao = jobDao;
        this.mailService = mailService;
    }

    public void configScheduler() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        try {
            GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
            Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(matcher);
            if(!triggerKeySet.isEmpty()) {
                triggerKeySet.forEach(triggerKey -> {
                    try {
                        scheduler.unscheduleJob(triggerKey);
                    } catch (SchedulerException e) {
                        LOG.error("" , e);
                    }
                });
            }    
            scheduler.clear();
        } catch (SchedulerException e) {
            LOG.error("" , e);
        }
        List<DashboardJob> jobList = jobDao.getJobList(adminUserId);
        for (DashboardJob job : jobList) {
            try {
                long startTimeStamp = job.getStartDate().getTime();
                long endTimeStamp = job.getEndDate().getTime();
                if (endTimeStamp < System.currentTimeMillis()) {
                    // Skip expired job
                    continue;
                }
                JobDetail jobDetail = JobBuilder.newJob(getJobExecutor(job)).withIdentity(job.getId().toString()).build();
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .startAt(new Date().getTime() < startTimeStamp ? job.getStartDate() : new Date())
                        .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExp()))
                        .endAt(job.getEndDate())
                        .build();
                jobDetail.getJobDataMap().put("job", job);
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                LOG.error("{} Job id: {}", e.getMessage(), job.getId());
            } catch (Exception e) {
                LOG.error("" , e);
            }
        }
    }

    private Class<? extends Job> getJobExecutor(DashboardJob job) {
        switch (job.getJobType()) {
            case "mail":
                return MailJobExecutor.class;
        }
        return null;
    }

    protected void sendMail(DashboardJob job) {
        jobDao.updateLastExecTime(job.getId(), new Date());
        try {
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_PROCESSING, "");
            mailService.sendDashboard(job);
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_FINISH, "");
        } catch (Exception e) {
            LOG.error("" , e);
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_FAIL, ExceptionUtils.getStackTrace(e));
        }
    }

    public ServiceStatus save(String userId, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardJob job = new DashboardJob();
        job.setUserId(userId);
        job.setName(jsonObject.getString("name"));
        job.setConfig(jsonObject.getString("config"));
        job.setCronExp(jsonObject.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jsonObject.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jsonObject.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            LOG.error("" , e);
        }
        job.setJobType(jsonObject.getString("jobType"));
        jobDao.save(job);
        configScheduler();
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus update(String userId, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardJob job = new DashboardJob();
        job.setId(jsonObject.getLong("id"));
        job.setName(jsonObject.getString("name"));
        job.setConfig(jsonObject.getString("config"));
        job.setCronExp(jsonObject.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jsonObject.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jsonObject.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            LOG.error("" , e);
        }
        job.setJobType(jsonObject.getString("jobType"));
        jobDao.update(job);
        configScheduler();
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus delete(String userId, Long id) {
        jobDao.delete(id);
        configScheduler();
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus exec(String userId, Long id) {
        DashboardJob job = jobDao.getJob(id);
        new Thread(() ->
                sendMail(job)
        ).start();
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configScheduler();
    }
}
