package com.example.posts.data.api

import com.example.posts.data.model.PostResponse
import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("public/v1/posts")
    fun getPosts(): Call<PostResponse>
}