package com.android.interviewapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.interviewapp.network.UserRepository
import com.android.interviewapp.utils.UIState

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class UserListViewModel(
    private val usersRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)
) : CoroutineScope by coroutineScope, ViewModel() {

    private val _usersLiveData: MutableLiveData<UIState> = MutableLiveData(UIState.LOADING())
    val usersLivaData: LiveData<UIState> get() = _usersLiveData

    fun subscribeToUsersInfo(pageno:Int) {
        _usersLiveData.postValue(UIState.LOADING())

        collectUserInfo()

        launch {
            usersRepository.getUsersList(pageno)
        }
    }

    private fun collectUserInfo() {
        launch {
            usersRepository.userList.collect { uiState ->
                when(uiState) {
                    is UIState.LOADING -> { _usersLiveData.postValue(uiState) }
                    is UIState.SUCCESS -> { _usersLiveData.postValue(uiState) }
                    is UIState.ERROR -> { _usersLiveData.postValue(uiState) }
                }
            }
        }
    }
}