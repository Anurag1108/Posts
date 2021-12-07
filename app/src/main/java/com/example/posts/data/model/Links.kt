package com.example.posts.data.model


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("current")
    var current: String? = null,
    @SerializedName("next")
    var next: String? = null,
    @SerializedName("previous")
    var previous: Any? = null
)