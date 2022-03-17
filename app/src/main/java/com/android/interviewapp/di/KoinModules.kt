package com.android.interviewapp.di

import com.android.interviewapp.network.UserApi
import com.android.interviewapp.network.UserRepository
import com.android.interviewapp.network.UserRepositoryImpl
import com.android.interviewapp.viewmodel.UserListViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    // provides the users repository implementation
    fun provideUsersRepo(usersApi: UserApi): UserRepository = UserRepositoryImpl(usersApi)

    // provide Gson object
    fun provideGson() = GsonBuilder().create()

    // provide logging interceptor
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    // provide okhttp client
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    // providing the retrofit builder
    fun provideUsersApi(okHttpClient: OkHttpClient, gson: Gson): UserApi =
        Retrofit.Builder()
            .baseUrl(UserApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(UserApi::class.java)

    single { provideGson() }
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideUsersApi(get(), get()) }
    single { provideUsersRepo(get()) }
}

val viewModelModule = module {
    viewModel { UserListViewModel(get()) }
}