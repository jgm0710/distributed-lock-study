package com.example.distributedlockstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributedLockStudyApplication

fun main(args: Array<String>) {
    runApplication<DistributedLockStudyApplication>(*args)
}
