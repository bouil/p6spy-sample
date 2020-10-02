package org.bouillon.p6spy.sample

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SampleEntityDao : JpaRepository<SampleEntity, UUID> {

}