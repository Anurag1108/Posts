package com.example.posts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posts.data.repository.PostRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var retrofitRepository: PostRepository? =
        PostRepository.getInstance(application)

    private var _postFirstPageResponse = MutableLiveData<Any>()
    private var _postNextPageResponse = MutableLiveData<Any>()

    fun requestFirstPageTopMovies() {
        retrofitRepository!!.loadPage(_postFirstPageResponse)
    }

    fun requestFirstNextPageMovies() {
        retrofitRepository!!.loadPage(_postNextPageResponse)
    }

    val postFirstPageResponse: LiveData<Any>
        get() = _postFirstPageResponse
    val postNextPageResponse: LiveData<Any>
        get() = _postNextPageResponse
}