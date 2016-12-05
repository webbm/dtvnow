package com.webb.dtvnow.service

interface DataService {
    
    fun fetchDataFromUrl(url: String) : String?
    
}
