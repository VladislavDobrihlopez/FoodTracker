package com.voitov.onboarding_domain.di

import com.voitov.onboarding_domain.use_cases.HandleNutrientPlanUseCase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingModule {
    @ViewModelScoped
    fun provideHandleNutrientPlanUseCase(): HandleNutrientPlanUseCase {
        return HandleNutrientPlanUseCase()
    }
}