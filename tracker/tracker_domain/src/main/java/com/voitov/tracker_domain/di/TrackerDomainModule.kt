package com.voitov.tracker_domain.di

import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import com.voitov.tracker_domain.use_case.DeleteFoodUseCase
import com.voitov.tracker_domain.use_case.DoNutrientMathUseCase
import com.voitov.tracker_domain.use_case.InsertFoodUseCase
import com.voitov.tracker_domain.use_case.NutrientStuffUseCasesWrapper
import com.voitov.tracker_domain.use_case.RetrieveAllFoodOnDateUseCase
import com.voitov.tracker_domain.use_case.SearchFoodUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {
    @ViewModelScoped
    @Provides
    fun provideUseCases(
        keyValueStorage: UserInfoKeyValueStorage,
        repository: FoodTrackerRepository
    ) =
        NutrientStuffUseCasesWrapper(
            DeleteFoodUseCase(repository),
            DoNutrientMathUseCase(keyValueStorage),
            InsertFoodUseCase(repository),
            RetrieveAllFoodOnDateUseCase(repository),
            SearchFoodUseCase(repository)
        )
}