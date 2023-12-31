package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCustomTrackableFoodUseCase @Inject constructor(
    private val repository: FoodTrackerRepository
) {
    operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 100,
    ): Flow<List<CustomTrackableFood>> {
        val formattedQuery = query.trim().lowercase()
        return repository.searchForCustomFood(
            formattedQuery.trim(),
            page,
            pageSize,
        ).map { items ->
            if (formattedQuery.isEmpty()) {
                items
            } else {
                items.filter { it.trackableFood.name.lowercase().contains(formattedQuery) }
            }
        }
    }
}