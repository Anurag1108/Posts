package com.example.posts.data.model


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("pagination")
    var pagination: Pagination? = null
)