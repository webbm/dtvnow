package com.webb.dtvnow.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
open class SlackNotificationService 

    @Autowired constructor(@Value("\${notify.slack.channel}") val slackChannel: String) : NotificationService {
    
    override fun notify(msg: String) {
        Runtime.getRuntime().exec(arrayOf("slack", slackChannel, msg))
    }
}
