package org.bouillon.p6spy.sample

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class SampleEntity {

    @Id
    var id: UUID = UUID.randomUUID()

    var field1: String? = null

}