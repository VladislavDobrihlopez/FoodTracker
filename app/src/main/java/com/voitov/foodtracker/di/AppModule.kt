package com.voitov.foodtracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.voitov.common.data.UserInfoPreferencesKeyValueStorage
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(UserInfoKeyValueStorage.STORAGE_LOCATION, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideKeyValueStorage(sharedPreferences: SharedPreferences): UserInfoKeyValueStorage {
        return UserInfoPreferencesKeyValueStorage(sharedPreferences)
    }
}