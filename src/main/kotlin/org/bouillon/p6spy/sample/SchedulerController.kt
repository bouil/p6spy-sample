package org.bouillon.p6spy.sample

import org.bouillon.p6spy.sample.job.GenerateDataJob
import org.quartz.*
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import javax.annotation.PostConstruct

@RestController
@RequestMapping("/scheduler")
class SchedulerController {

    @Autowired
    private lateinit var scheduler: Scheduler

    @PostConstruct
    fun postConstruct() {
        this.scheduleJob("Generate data")
    }

    @GetMapping(produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getScheduledJobs(): String {
        val sb = StringBuilder()
        sb.appendLine("""Schedule is clustered: ${scheduler.metaData.isJobStoreClustered}""")
        val jobGroupNames = scheduler.jobGroupNames
        sb.appendLine("""List of Jobs (${jobGroupNames.size} found):""")
        for (groupName: String? in jobGroupNames) {
            for (jobKey: JobKey in scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                val jobName = jobKey.name
                val jobGroup = jobKey.group
                sb.appendLine(
                    """- [jobName] : $jobName - [groupName] : $jobGroup"""
                )
                //get job's trigger
                val triggers: List<Trigger> = scheduler.getTriggersOfJob(jobKey) as List<Trigger>
                triggers.forEach { trigger ->
                    sb.appendLine(
                        """     - [TriggerClass] ${trigger.javaClass} - [NextFireTime] ${trigger.nextFireTime}"""
                    )

                }
            }
        }
        return sb.toString();
    }

    @GetMapping(path = ["/add/{name}"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun scheduleJob(@PathVariable("name") name: String): String {
        val jobDetail = buildJobDetail(name)
        val jobTrigger = buildJobTrigger(jobDetail, ZonedDateTime.now().plusSeconds(2))
        val scheduleJob: Date = scheduler.scheduleJob(jobDetail, jobTrigger)

        return "$scheduleJob $jobDetail"
    }

    @GetMapping(path = ["/run/{group}/{name}"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun runNow(@PathVariable("group") group: String, @PathVariable("name") name: String): String {
        val jobKey = JobKey(name, group)
        val jobDetail = scheduler.getJobDetail(jobKey)
        scheduler.triggerJob(jobKey)
        return "$jobDetail"
    }

    private fun buildJobDetail(name: String): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap.put("now", LocalDateTime.now())
        return JobBuilder.newJob(GenerateDataJob::class.java)
            .withIdentity(name, "data-trigger")
            .withDescription("Generate SQL Data")
            .usingJobData(jobDataMap)
            .storeDurably()
            .build()
    }

    private fun buildJobTrigger(jobDetail: JobDetail, startAt: ZonedDateTime): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, "data-trigger")
            .withDescription("Generate SQL Data")
            .startAt(Date.from(startAt.toInstant()))
            .withSchedule(
                SimpleScheduleBuilder.repeatSecondlyForever(10).withMisfireHandlingInstructionIgnoreMisfires()
            )
            .build()
    }

}