package com.webb.dtvnow

import com.nhaarman.mockito_kotlin.*
import com.webb.dtvnow.channel.Channel
import com.webb.dtvnow.service.DataService
import com.webb.dtvnow.service.NotificationService
import com.webb.dtvnow.service.ResultsSaveService
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.endsWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import kotlin.test.assertFalse

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class AppTest {
    
    lateinit var sut: App
    
    @Mock
    lateinit var dataFile: File
    
    lateinit var channels: Set<Channel>
    
    @Mock
    lateinit var dataService: DataService
    
    @Mock
    lateinit var resultsSaveService: ResultsSaveService
    
    @Mock
    lateinit var notificationService: NotificationService
    
    private fun createSUT(channels: Set<Channel> = mock()) : App {
        return App(dataFile, channels, dataService, resultsSaveService, notificationService)
    }
    
    @Test
    fun testRun_AllAlreadyUpdated() {
        
        channels = setOf(
                Channel(name = "foo", url = "http://foo/url", present = true),
                Channel(name = "bar", url = "http://bar/url", present = true)
        )
        
        sut = createSUT(channels)
        
        sut.run()
        
        verify(notificationService, never()).notify(endsWith("now supports DirecTV NOW as a sign-in provider."))
        verify(notificationService).notify("All channels now support DirecTV NOW as a provider!")
        verifyZeroInteractions(dataService, resultsSaveService)
    }

    @Test
    fun testRun_NoneAlreadyUpdated_NoneYetSupported() {
        
        val fooUrl = "http://foo/url"
        val barUrl = "http://bar/url"
        
        val fooResponse = "{\"foo\" : \"json\"}"
        val barResponse = "{\"bar\" : \"json\"}"
        
        whenever(dataService.fetchDataFromUrl(fooUrl)).thenReturn(fooResponse)
        whenever(dataService.fetchDataFromUrl(barUrl)).thenReturn(barResponse)

        channels = setOf(
                Channel(name = "foo", url = fooUrl, present = false),
                Channel(name = "bar", url = barUrl, present = false)
        )

        sut = createSUT(channels)

        sut.run()

        verify(dataService).fetchDataFromUrl(fooUrl)
        verify(dataService).fetchDataFromUrl(barUrl)
        
        verify(notificationService, never()).notify(endsWith("now supports DirecTV NOW as a sign-in provider."))
        verify(notificationService, never()).notify("All channels now support DirecTV NOW as a provider!")
        
        verify(resultsSaveService).save(eq(dataFile), any())
    }

    @Test
    fun testRun_NoneAlreadyUpdated_AllNowSupported() {

        val fooUrl = "http://foo/url"
        val barUrl = "http://bar/url"

        val fooResponse = "{\"foo\" : \"json\", \"supports\" : \"DirecTV NOW\"}"
        val barResponse = "{\"bar\" : \"json\", \"supports\" : \"DirecTV NOW\"}"

        whenever(dataService.fetchDataFromUrl(fooUrl)).thenReturn(fooResponse)
        whenever(dataService.fetchDataFromUrl(barUrl)).thenReturn(barResponse)

        channels = setOf(
                Channel(name = "foo", url = fooUrl, present = false),
                Channel(name = "bar", url = barUrl, present = false)
        )

        sut = createSUT(channels)

        sut.run()

        verify(dataService).fetchDataFromUrl(fooUrl)
        verify(dataService).fetchDataFromUrl(barUrl)

        verify(notificationService).notify("foo, bar now supports DirecTV NOW as a sign-in provider.")
        verify(notificationService, never()).notify("All channels now support DirecTV NOW as a provider!")

        verify(resultsSaveService).save(eq(dataFile), any())
    }
    
    @Test
    fun testSupportsDirecTVNow_Yes() {
        sut = createSUT()
        val result = sut.supportsDirecTVNow("directv now")
        assertTrue(result)
    }

    @Test
    fun testSupportsDirecTVNow_No() {
        sut = createSUT()
        val result = sut.supportsDirecTVNow("directv")
        assertFalse(result)
    }
    
}
