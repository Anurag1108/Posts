package com.example.posts.data.model


import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("data")
    var `data`: List<Data>? = null,
    @SerializedName("meta")
    var meta: Meta? = null
)