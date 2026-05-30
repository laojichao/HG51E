package com.aotem.hg51e.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aotem.hg51e.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var queryJob: Job? = null
    private var toastShown = false

    fun queryUser(host: String) {
        _uiState.value = UiState.Loading
        toastShown = false
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            try {
                val user = userRepository.queryUser(host)
                if (user.data.webTeleAccountName.isEmpty() && user.data.teleAccountPassword.isEmpty()) {
                    _uiState.value = UiState.Error("数据解析失败", shouldShowToast = !toastShown)
                } else {
                    _uiState.value = UiState.Success(user, shouldShowToast = !toastShown)
                }
                toastShown = true
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "未知错误", shouldShowToast = !toastShown)
                toastShown = true
            }
        }
    }
}
