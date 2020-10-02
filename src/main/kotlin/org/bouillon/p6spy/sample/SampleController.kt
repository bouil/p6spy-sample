package org.bouillon.p6spy.sample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/sample")
class SampleController {

    @Autowired
    private lateinit var sampleEntityDao: SampleEntityDao

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): MutableList<SampleEntity> {
        return sampleEntityDao.findAll()
    }

    @GetMapping(path = ["/{uuid}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable("uuid") uuid: UUID): Optional<SampleEntity> {
        return sampleEntityDao.findById(uuid)
    }

}