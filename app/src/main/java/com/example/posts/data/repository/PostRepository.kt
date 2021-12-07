package com.example.posts.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.posts.data.api.API
import com.example.posts.data.api.ApiClient
import com.example.posts.data.model.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository private constructor (app: Application) {

    private var instanceApi: API

    init {
        ApiClient.init(app)
        instanceApi = ApiClient.instance
    }

    companion object {
        private var postRepository: PostRepository? = null

        @Synchronized
        fun getInstance(app: Application): PostRepository? {
            if (postRepository == null) {
                postRepository = PostRepository(app)
            }
            return postRepository
        }
    }

    fun loadPage(postResponse: MutableLiveData<Any>) {
        instanceApi.getPosts().enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                postResponse.value = t
            }

            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {

                if (response.isSuccessful) {
                    postResponse.value = response.body()
                } else {
                    postResponse.value = "error"
                }
            }
        })
    }
}
