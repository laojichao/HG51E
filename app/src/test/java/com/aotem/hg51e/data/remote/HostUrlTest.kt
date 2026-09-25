package com.aotem.hg51e.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HostUrlTest {

    @Test
    fun `plain ip is normalized to http url with trailing slash`() {
        assertEquals("http://192.168.1.1/", HostUrl.normalize("192.168.1.1").toString())
    }

    @Test
    fun `blank or null input falls back to default host`() {
        assertEquals("http://192.168.1.1/", HostUrl.normalize(null).toString())
        assertEquals("http://192.168.1.1/", HostUrl.normalize("").toString())
        assertEquals("http://192.168.1.1/", HostUrl.normalize("   ").toString())
    }

    @Test
    fun `input with scheme and trailing slash is kept`() {
        assertEquals("http://192.168.1.1/", HostUrl.normalize("http://192.168.1.1/").toString())
        assertEquals("https://192.168.1.1/", HostUrl.normalize("https://192.168.1.1").toString())
    }

    @Test
    fun `whitespace around input is trimmed`() {
        assertEquals("http://192.168.1.1/", HostUrl.normalize(" 192.168.1.1 ").toString())
    }

    @Test
    fun `port is preserved`() {
        assertEquals("http://192.168.1.1:8080/", HostUrl.normalize("192.168.1.1:8080").toString())
    }

    @Test
    fun `invalid input returns null`() {
        assertNull(HostUrl.normalize("http://"))
        assertNull(HostUrl.normalize("http://:::"))
    }
}
