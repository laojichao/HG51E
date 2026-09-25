package com.aotem.hg51e.ui.main

import com.aotem.hg51e.data.model.User

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success(val user: User) : UiState
    data class Error(val message: String) : UiState
}
