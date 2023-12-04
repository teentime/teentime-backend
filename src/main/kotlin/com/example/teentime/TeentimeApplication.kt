package com.example.teentime

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class TeentimeApplication

fun main(args: Array<String>) {
    runApplication<TeentimeApplication>(*args)
}
