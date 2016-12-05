package com.webb.dtvnow.service

import java.io.File

interface ResultsSaveService {
    
    fun save(file: File, content: String)
    
}
