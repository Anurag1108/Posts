package com.example.posts.data.model


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("limit")
    var limit: Int? = null,
    @SerializedName("links")
    var links: Links? = null,
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("pages")
    var pages: Int? = null,
    @SerializedName("total")
    var total: Int? = null
)