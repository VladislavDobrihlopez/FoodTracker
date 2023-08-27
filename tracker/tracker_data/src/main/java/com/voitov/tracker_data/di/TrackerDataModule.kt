package com.voitov.tracker_data.di

import android.app.Application
import com.voitov.tracker_data.local.db.TrackedFoodDatabase
import com.voitov.tracker_data.remote.BaseCountryHolder
import com.voitov.tracker_data.remote.OpenFoodApiService
import com.voitov.tracker_data.repository.FoodTrackerRepositoryImpl
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerDataModule {
//    @Singleton
//    @Binds
//    abstract fun bindTrackerRepository(impl: FoodTrackerRepositoryImpl): FoodTrackerRepository

    companion object {
        @JvmSuppressWildcards
        @Singleton
        @Provides
        fun provideTrackedRepository(
            db: TrackedFoodDatabase,
            apiService: OpenFoodApiService,
            countryUrlHolders: List<BaseCountryHolder>
        ): FoodTrackerRepository {
            return FoodTrackerRepositoryImpl(apiService, db.trackedFoodDao(), countryUrlHolders)
        }

        @Singleton
        @Provides
        fun provideDao(db: TrackedFoodDatabase) = db.trackedFoodDao()

        @Singleton
        @Provides
        fun provideHttpInterceptor() = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        @Singleton
        @Provides
        fun provideOpenFoodApi(client: OkHttpClient): OpenFoodApiService {
            return Retrofit.Builder()
                .baseUrl(OpenFoodApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(OpenFoodApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideTrackedFoodDatabase(context: Application): TrackedFoodDatabase {
            return TrackedFoodDatabase.getInstance(context)
        }

        @Provides
        fun provideUrlHolders(): List<BaseCountryHolder> {
            return listOf(
                BaseCountryHolder.Belarus(),
                BaseCountryHolder.Ukraine(),
                BaseCountryHolder.Germany(),
                BaseCountryHolder.Poland(),
                BaseCountryHolder.Russia(),
                BaseCountryHolder.USA(),
                BaseCountryHolder.UK(),
                BaseCountryHolder.World(),
            )
        }
    }
}