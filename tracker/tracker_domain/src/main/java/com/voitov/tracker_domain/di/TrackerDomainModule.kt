package com.voitov.tracker_domain.di

import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import com.voitov.tracker_domain.use_case.DeleteTrackableCustomFoodUseCase
import com.voitov.tracker_domain.use_case.DeleteTrackedFoodUseCase
import com.voitov.tracker_domain.use_case.DoNutrientMathUseCase
import com.voitov.tracker_domain.use_case.GetAccumulatedFoodEachDayUseCase
import com.voitov.tracker_domain.use_case.InsertTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.RestoreFoodUseCase
import com.voitov.tracker_domain.use_case.RetrieveAllTrackedFoodOnDateUseCase
import com.voitov.tracker_domain.use_case.SearchCustomTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.SearchTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.wrapper.NutrientStuffUseCasesWrapper
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
            DeleteTrackedFoodUseCase(repository),
            DoNutrientMathUseCase(keyValueStorage),
            InsertTrackableFoodUseCase(repository),
            RestoreFoodUseCase(repository),
            RetrieveAllTrackedFoodOnDateUseCase(repository),
            SearchTrackableFoodUseCase(repository),
            SearchCustomTrackableFoodUseCase(repository),
            DeleteTrackableCustomFoodUseCase(repository),
            GetAccumulatedFoodEachDayUseCase(repository),
        )
}