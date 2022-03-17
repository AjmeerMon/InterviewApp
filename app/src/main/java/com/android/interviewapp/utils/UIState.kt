package com.android.interviewapp.utils

import com.android.interviewapp.model.UserData


sealed class UIState {
    class SUCCESS(val success: UserData): UIState()
    class LOADING(val isLoading: Boolean = true) : UIState()
    class ERROR(val error: Throwable): UIState()
}