package com.aotem.hg51e.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aotem.hg51e.data.repository.UserRepository
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 一次性提示事件：不重放，避免旋转屏幕重收集时 toast 重复弹出
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    private var queryJob: Job? = null

    fun queryUser(host: String) {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val user = userRepository.queryUser(host)
                if (user.data.webTeleAccountName.isEmpty() && user.data.teleAccountPassword.isEmpty()) {
                    onError("数据解析失败，可能固件已加密或型号不匹配")
                } else {
                    _uiState.value = UiState.Success(user)
                    _messages.tryEmit("获取成功")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onError(e.toUserMessage())
            }
        }
    }

    private fun onError(message: String) {
        _uiState.value = UiState.Error(message)
        _messages.tryEmit(message)
    }

    private fun Exception.toUserMessage(): String = when (this) {
        is JsonSyntaxException -> "返回数据解析失败，可能固件已加密或型号不匹配"
        is UnknownHostException -> "无法连接到光猫，请检查网络和地址"
        is ConnectException -> "无法连接到光猫，请检查网络和地址"
        is SocketTimeoutException -> "连接超时，请重试"
        else -> message ?: "未知错误"
    }
}
