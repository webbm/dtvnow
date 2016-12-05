package com.webb.dtvnow

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class DtvnowApplication {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(DtvnowApplication::class.java, *args)
        }
    }
}

