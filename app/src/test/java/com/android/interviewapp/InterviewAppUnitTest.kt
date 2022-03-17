package com.android.interviewapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.interviewapp.model.UserData
import com.android.interviewapp.network.UserRepository
import com.android.interviewapp.utils.UIState
import com.android.interviewapp.utils.Utils
import com.android.interviewapp.viewmodel.UserListViewModel
import com.google.gson.Gson
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class InterviewAppUnitTest {


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val testCoroutineRule = TestCoroutinesRule()

    @Mock
    lateinit var getUsersList: UserRepository
    private val outputdata:UserData= Gson().fromJson(Utils.outputstring, UserData::class.java)
    @Mock
    lateinit var observer: Observer<UIState>

    private lateinit var viewModel: UserListViewModel
    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = UserListViewModel(getUsersList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `make sure the result is not null`() =
        testCoroutineRule.runBlockingTest {
            val expectedOutput = UIState.SUCCESS(outputdata)
            viewModel.usersLivaData.observeForever(observer)
            Mockito.`when`(viewModel.usersLivaData.value).thenAnswer {
                flowOf(expectedOutput)
            }
            viewModel.subscribeToUsersInfo(2)

            Assert.assertNotNull(viewModel.usersLivaData.value)
        }
}