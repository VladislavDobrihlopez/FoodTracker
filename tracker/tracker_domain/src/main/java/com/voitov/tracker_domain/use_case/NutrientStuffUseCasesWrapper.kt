package com.voitov.tracker_domain.use_case

data class NutrientStuffUseCasesWrapper(
    val deleteFoodUseCase: DeleteFoodUseCase,
    val doNutrientMathUseCase: DoNutrientMathUseCase,
    val insertFoodUseCase: InsertFoodUseCase,
    val retrieveAllFoodOnDateUseCase: RetrieveAllFoodOnDateUseCase,
    val searchFoodUseCase: SearchFoodUseCase
)