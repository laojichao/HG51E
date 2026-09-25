package com.aotem.hg51e.data.remote

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * 将用户输入的路由地址规范化为可直接使用的 baseUrl。
 */
object HostUrl {

    const val DEFAULT_HOST = "192.168.1.1"

    /**
     * 输入为空时回退到默认地址；未携带协议时补充 http://；
     * 无法解析为合法 URL 时返回 null。
     */
    fun normalize(raw: String?): HttpUrl? {
        val input = raw?.trim().orEmpty().ifEmpty { DEFAULT_HOST }
        val withScheme = when {
            input.startsWith("http://") || input.startsWith("https://") -> input
            else -> "http://$input"
        }
        return withScheme.toHttpUrlOrNull()
    }
}
