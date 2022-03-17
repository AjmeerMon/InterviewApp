package com.android.interviewapp.network
import com.android.interviewapp.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userList: StateFlow<UIState>
    suspend fun getUsersList(offset:Int)
}

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private val _userListFlow: MutableStateFlow<UIState> = MutableStateFlow(UIState.LOADING())

    override val userList: StateFlow<UIState>
        get() = _userListFlow

    override suspend fun getUsersList(pageno:Int) {
        try {
            val response = userApi.getUserList(pageno)

            if (response.isSuccessful) {
                response.body()?.let {
                    _userListFlow.value = UIState.SUCCESS(it)
                } ?: run {
                    _userListFlow.value = UIState.ERROR(IllegalStateException("users are coming as null!"))
                }

            } else {
                _userListFlow.value = UIState.ERROR(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            _userListFlow.value = UIState.ERROR(e)
        }
    }


}