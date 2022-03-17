package com.android.interviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.android.interviewapp.adapters.UsersAdapter
import com.android.interviewapp.databinding.ActivityMainBinding
import com.android.interviewapp.model.UserData
import com.android.interviewapp.utils.PaginationScrollListener
import com.android.interviewapp.utils.UIState
import com.android.interviewapp.utils.Utils
import com.android.interviewapp.viewmodel.UserListViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

    private var loadedpage:Int = 0

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val userListViewModel: UserListViewModel by viewModel()

    private lateinit var adapter: UsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter=UsersAdapter()

        binding.rcvUserlist.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter =adapter
        }

        binding.rcvUserlist.addOnScrollListener(object : PaginationScrollListener(binding.rcvUserlist.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                if(!isLastPage) {
                    isLoading = true
                    currentPage += 1
                    loadPage()
                }
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        /******
         *  ! Important - If your want to perform UI test please uncomment the below three lines
         */
        //val outputdata: UserData = Gson().fromJson(Utils.outputstring, UserData::class.java)
        //handleResults(UIState.SUCCESS(outputdata))



        /******
         *  ! Important - If your want to perform UI test please comment the below two lines
         */
        userListViewModel.usersLivaData.observe(this, ::handleResults)
        loadPage()
    }

    fun loadPage() {
        userListViewModel.subscribeToUsersInfo(currentPage)
    }

    private fun handleResults(uiState: UIState) {
        when(uiState) {
            is UIState.LOADING -> {
                binding.progressbar.visibility = View.VISIBLE
            }
            is UIState.SUCCESS -> {
                binding.progressbar.visibility = View.GONE
                isLoading = false
                totalPages = uiState.success.totalPages
                if(loadedpage!=uiState.success.page) {
                    loadedpage=uiState.success.page
                    adapter.addAll(uiState.success.data)
                }
                if (currentPage != totalPages) binding.progressbar.visibility=View.VISIBLE
                else isLastPage = true
            }
            is UIState.ERROR -> {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(this, "Please retry again!", Toast.LENGTH_LONG).show()
            }
        }
    }
}