package com.voitov.tracker_data.remote

import com.voitov.tracker_data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Url

interface OpenFoodApiService {
    @GET
    suspend fun searchForProducts(
        @Url url: String
    ): SearchDto

    companion object {
        const val BASE_URL = "https://us.openfoodfacts.org/"

        const val COUNTRY_PATH = "country"
        const val SEARCH_TERMS_QUERY = "search_terms"
        const val PAGE_QUERY = "page"
        const val PAGE_SIZE_QUERY = "page_size"
        const val BASE_URL_FOR_FORMATTING = "https://$COUNTRY_PATH.openfoodfacts.org/cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments,image_front_thumb_url"
    }
}