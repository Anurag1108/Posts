package com.example.posts.data.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("body")
    var body: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("user_id")
    var userId: Int? = null
)