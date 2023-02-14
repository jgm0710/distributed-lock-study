package com.example.distributedlockstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@SpringBootApplication
class DistributedLockStudyApplication

fun main(args: Array<String>) {
    runApplication<DistributedLockStudyApplication>(*args)
}
