package com.voitov.tracker_domain.use_case.wrapper

import com.voitov.tracker_domain.use_case.DeleteTrackableCustomFoodUseCase
import com.voitov.tracker_domain.use_case.DeleteTrackedFoodUseCase
import com.voitov.tracker_domain.use_case.DoNutrientMathUseCase
import com.voitov.tracker_domain.use_case.InsertTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.RestoreFoodUseCase
import com.voitov.tracker_domain.use_case.RetrieveAllTrackedFoodOnDateUseCase
import com.voitov.tracker_domain.use_case.SearchCustomTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.SearchTrackableFoodUseCase

data class NutrientStuffUseCasesWrapper(
    val deleteTrackedFoodUseCase: DeleteTrackedFoodUseCase,
    val doNutrientMathUseCase: DoNutrientMathUseCase,
    val insertTrackableFoodUseCase: InsertTrackableFoodUseCase,
    val restoreFoodUseCase: RestoreFoodUseCase,
    val retrieveAllTrackedFoodOnDateUseCase: RetrieveAllTrackedFoodOnDateUseCase,
    val searchTrackableFoodUseCase: SearchTrackableFoodUseCase,
    val searchCustomTrackableFoodUseCase: SearchCustomTrackableFoodUseCase,
    val deleteTrackableCustomFoodUseCase: DeleteTrackableCustomFoodUseCase
)