package com.example.posts.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.R
import com.example.posts.data.model.Data
import com.example.posts.data.model.Pagination
import com.example.posts.data.model.PostResponse
import com.example.posts.databinding.ActivityHomeBinding
import com.example.posts.utils.CheckValidation
import com.example.posts.utils.MessageLog
import com.example.posts.utils.PaginationScrollListener
import com.example.posts.viewmodels.HomeViewModel
import com.example.posts.views.adapters.PostPaginationAdapter
import java.util.concurrent.TimeoutException

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAdapter: PostPaginationAdapter
    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.activity = this

        homeViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(HomeViewModel::class.java)

        initMyOrderRecyclerView()
        observerDataRequest()
    }
    private fun initMyOrderRecyclerView() {
        //attach adapter to  recycler
        mAdapter = PostPaginationAdapter(this@HomeActivity)
        binding.adapterPosts = mAdapter
        binding.recyclerMyOrders.setHasFixedSize(true)
        binding.recyclerMyOrders.itemAnimator = DefaultItemAnimator()

        loadFirstPage()

        binding.recyclerMyOrders.addOnScrollListener(object : PaginationScrollListener(binding.recyclerMyOrders.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.myLooper()!!).postDelayed({
                    loadNextPage()
                }, 1000)
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
    }
    private fun loadFirstPage() {
        hideErrorView()
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstPageTopMovies()
        }else{
            showErrorView(null)
        }
    }

    fun loadNextPage() {
        if (CheckValidation.isConnected(this)) {
            homeViewModel.requestFirstNextPageMovies()
        }else{
            mAdapter.showRetry(true, fetchErrorMessage(null))
        }
    }

    private fun observerDataRequest(){
        homeViewModel.postFirstPageResponse.observe(this) {
            if (it is PostResponse) {
                hideErrorView()
                val results: MutableList<Data> = fetchResults(it) as MutableList<Data>
                binding.mainProgress.visibility = View.GONE
                mAdapter.addAll(results)
                totalPages = it.meta!!.pagination!!.pages!!

                if (currentPage <= totalPages) mAdapter.addLoadingFooter()
                else isLastPage = true
            }else if (it is Throwable){
                showErrorView(it)
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page")
            }
        }

        homeViewModel.postNextPageResponse.observe(this) {
            if (it is PostResponse) {

                val results = fetchResults(it) as MutableList<Data>
                mAdapter.removeLoadingFooter()
                isLoading = false
                mAdapter.addAll(results)

                if (currentPage != totalPages) mAdapter.addLoadingFooter()
                else isLastPage = true

            }else if (it is Throwable){
                mAdapter.showRetry(true, fetchErrorMessage(it))
            }else{
                MessageLog.setLogCat("TAG_TEST" , "Error First Page")
            }

        }
    }

    private fun fetchResults(moviesTopModel: PostResponse): List<Data>? {
        return moviesTopModel.data
    }

    private fun showErrorView(throwable: Throwable?) {
        if (binding.lyError.errorLayout.visibility == View.GONE) {
            binding.lyError.errorLayout.visibility = View.VISIBLE
            binding.mainProgress.visibility = View.GONE

            if (!CheckValidation.isConnected(this)) {
                binding.lyError.errorTxtCause.setText(R.string.error_msg_no_internet)
            } else {
                if (throwable is TimeoutException) {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_timeout)
                } else {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_unknown)
                }
            }
        }
    }

    private fun hideErrorView() {
        if (binding.lyError.errorLayout.visibility == View.VISIBLE) {
            binding.lyError.errorLayout.visibility = View.GONE
            binding.mainProgress.visibility = View.VISIBLE
        }
    }

    private fun fetchErrorMessage(throwable: Throwable?): String {
        var errorMsg: String = resources.getString(R.string.error_msg_unknown)

        if (!CheckValidation.isConnected(this)) {
            errorMsg = resources.getString(R.string.error_msg_no_internet)
        } else if (throwable is TimeoutException) {
            errorMsg = resources.getString(R.string.error_msg_timeout)
        }

        return errorMsg
    }
}