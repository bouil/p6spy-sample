package org.bouillon.p6spy.sample.job

import org.bouillon.p6spy.sample.SampleEntityService
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.transaction.annotation.Transactional

@DisallowConcurrentExecution
open class GenerateDataJob(private val sampleEntityService: SampleEntityService) : QuartzJobBean() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(GenerateDataJob::class.java)
    }

    @Transactional
    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        val sampleEntity = sampleEntityService.createNew()
        log.info("Saved new sampleEntity $sampleEntity")
    }

}