package com.webb.dtvnow.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.webb.dtvnow.channel.Channel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
@ComponentScan("com.webb.dtvnow")
open class AppConfig {
    
    @Value("\${file.location}")
    lateinit var filePath: String
    
    @Bean
    open fun channelList() : Set<Channel> {
        val jsonFile = dataFile()
        val rawJson = jsonFile.readText()
        val om = ObjectMapper()
        val channels: Set<Channel> = om.readValue(rawJson, object : TypeReference<Set<Channel>>(){})
        return channels
    }
    
    @Bean
    open fun dataFile() : File {
        val file = File(filePath)
        if (!file.isFile) {
            throw RuntimeException("$filePath isn't a valid file path!")
        }
        if (!file.canWrite()) {
            throw RuntimeException("$filePath must be writable!")
        }
        return file;
    }

}
