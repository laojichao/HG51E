package com.aotem.hg51e.ui.main

import androidx.lifecycle.ViewModel
import com.aotem.hg51e.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()
    private var toastShown = false

    fun queryUser(host: String) {
        _uiState.value = UiState.Loading
        toastShown = false
        disposables.clear()
        disposables.add(
            userRepository.queryUser(host)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user ->
                        if (user.data == null) {
                            _uiState.value = UiState.Error("数据解析失败", shouldShowToast = !toastShown)
                        } else {
                            _uiState.value = UiState.Success(user, shouldShowToast = !toastShown)
                        }
                        toastShown = true
                    },
                    { e ->
                        _uiState.value = UiState.Error(e.message ?: "未知错误", shouldShowToast = !toastShown)
                        toastShown = true
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
