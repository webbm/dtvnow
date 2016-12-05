package com.webb.dtvnow.service

import org.springframework.stereotype.Service
import java.net.URL

@Service
open class DefaultDataService : DataService {
    
    override fun fetchDataFromUrl(url: String): String? {
        try {
            return URL(url).openStream().bufferedReader().readText()
        }
        catch (e: Exception) {
            return null
        }
    }
    
}
