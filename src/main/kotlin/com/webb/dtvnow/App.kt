package com.webb.dtvnow

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Sets
import com.webb.dtvnow.channel.Channel
import com.webb.dtvnow.service.DataService
import com.webb.dtvnow.service.NotificationService
import com.webb.dtvnow.service.ResultsSaveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.File
import java.util.regex.Pattern

@Component
open class App

    @Autowired constructor(val dataFile: File,
                           val channelList: Set<Channel>,
                           val dataService: DataService,
                           val resultsSaveService: ResultsSaveService,
                           val notificationService: NotificationService) : CommandLineRunner {
    
    companion object {
        val DTV_NOW_REGEX: Pattern = Pattern.compile(".*directv\\s?now.*", Pattern.CASE_INSENSITIVE)
    }
    
    override fun run(vararg p0: String?) {

        val total = channelList.size
        
        val alreadyUpdated = channelList.filter { it.present }.toSet()
        
        if (alreadyUpdated.size != total) {
            //we still have some to do

            val newlyUpdated = channelList.filter { !it.present } .map {
                println("Checking to see if ${it.name} supports DirecTV NOW as a provider...")
                val rawData = dataService.fetchDataFromUrl(it.url)
                val present = if (rawData is String) {
                    supportsDirecTVNow(rawData)
                }
                else {
                    System.err.println("There was an error retrieving data for ${it.name} from ${it.url}")
                    false
                }
                Channel(it.name, it.url, present)
            }.toSet()
            
            val newChannels = newlyUpdated.filter(Channel::present).map { it.name }
            
            val names = if (newChannels.isEmpty()) null else newChannels.reduce { left, right -> "$left, $right" }
            
            if (names is String) {
                notificationService.notify("$names now supports DirecTV NOW as a sign-in provider.")
            }
            
            val combined: MutableSet<Channel> = Sets.newHashSet()
            combined.addAll(alreadyUpdated)
            combined.addAll(newlyUpdated)

            println("Saving ${combined.size} channels.")
            
            val om = ObjectMapper()
            val json = om.writeValueAsString(combined)
            
            resultsSaveService.save(dataFile, json)
            
        }
        else {
            notificationService.notify("All channels now support DirecTV NOW as a provider!")
        }
    }
    
    fun supportsDirecTVNow(data: String) : Boolean {
        return DTV_NOW_REGEX.matcher(data).matches()
    }
}
