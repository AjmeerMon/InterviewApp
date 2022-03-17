package com.android.interviewapp.network

import com.android.interviewapp.model.UserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET(USER_LIST)
    suspend fun getUserList(
        @Query("page")pageno:Int
    ): Response<UserData>

    companion object {
        const val BASE_URL = "https://reqres.in/api/"
        private const val USER_LIST = "users"
    }
}