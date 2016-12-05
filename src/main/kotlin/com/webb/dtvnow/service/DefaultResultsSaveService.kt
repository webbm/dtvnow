package com.webb.dtvnow.service

import org.springframework.stereotype.Service
import java.io.File

@Service
open class DefaultResultsSaveService : ResultsSaveService {
    
    override fun save(file: File, content: String) {
        try {
            file.printWriter().use { out -> 
                out.write(content)
            }
        }
        catch (e: Exception) {
            System.err.println("There was an error while saving the file! Error: ${e.message}.")
        }
    }
    
}
