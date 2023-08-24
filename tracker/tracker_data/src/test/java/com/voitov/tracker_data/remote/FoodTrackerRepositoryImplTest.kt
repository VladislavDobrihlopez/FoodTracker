package com.voitov.tracker_data.remote

import com.google.common.truth.Truth.assertThat
import com.voitov.tracker_data.local.db.TrackedFoodDao
import com.voitov.tracker_data.repository.FoodTrackerRepositoryImpl
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FoodTrackerRepositoryImplTest {
    private lateinit var webServer: MockWebServer
    private lateinit var repository: FoodTrackerRepository

    @Before
    fun setUp() {
        webServer = MockWebServer()
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .build()

        val api = Retrofit.Builder()
            .baseUrl(webServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(OpenFoodApiService::class.java)

        repository =
            FoodTrackerRepositoryImpl(apiService = api, dao = mockk<TrackedFoodDao>(relaxed = true))
    }

    @Test
    fun `Search for products, valid response, must return results`() = runBlocking {
        webServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(SERVER_CORRECT_RESPONSE)
        })

        val result = repository.searchForTrackableFood(
            query = "integer",
            page = 1000,
            pageSize = 10,
            lowerBoundCoefficient = 0.95f,
            upperBoundCoefficient = 1.05f
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Search for products, invalid response, must fail`() = runBlocking {
        webServer.enqueue(MockResponse().apply {
            setResponseCode(400)
            setBody(SERVER_INCORRECT_RESPONSE)
        })

        val result = repository.searchForTrackableFood(
            query = "integer",
            page = 1000,
            pageSize = 10,
            lowerBoundCoefficient = 0.95f,
            upperBoundCoefficient = 1.05f
        )

        assertThat(result.isFailure).isTrue()
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }
}