package com.voitov.tracker_data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("image_front_thumb_url")
    val imageFrontThumbUrl: String?,
    val nutriments: Nutriments,
    @SerializedName("product_name")
    val productName: String?
)