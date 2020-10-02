package org.bouillon.p6spy.sample

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
open class SampleEntityService(private val sampleEntityDao: SampleEntityDao) {


    @Transactional(propagation = Propagation.MANDATORY)
    open fun createNew(): SampleEntity {
        return sampleEntityDao.save(SampleEntity())
    }

}