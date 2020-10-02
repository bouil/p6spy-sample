package org.bouillon.p6spy.sample

import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.annotation.PostConstruct


fun main(args: Array<String>) {
    SpringApplication.run(P6SpySampleConfiguration::class.java, *args)
}


@SpringBootApplication(scanBasePackageClasses = [P6SpySampleConfiguration::class])
@EnableTransactionManagement
open class P6SpySampleConfiguration {

    @Autowired
    private lateinit var sampleEntityDao: SampleEntityDao

    @Autowired
    private lateinit var scheduler: Scheduler

    @PostConstruct
    open fun postConstruct() {
    }

}