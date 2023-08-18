package com.voitov.foodtracker.navigation

sealed class AppNavState(val route: String) {
    object Welcome : AppNavState(WELCOME_ROUTE)
    object Age : AppNavState(AGE_ROUTE)
    object Gender : AppNavState(GENDER_ROUTE)
    object Height : AppNavState(HEIGHT_ROUTE)
    object Weight : AppNavState(WEIGHT_ROUTE)
    object NutrientGoal : AppNavState(NUTRIENT_GOAL_ROUTE)
    object Activity : AppNavState(ACTIVITY_ROUTE)
    object Goal : AppNavState(GOAL_ROUTE)
    object TrackerOverview : AppNavState(TRACKER_OVERVIEW_ROUTE)
    object Search : AppNavState(SEARCH_ROUTE)
    companion object {
        const val WELCOME_ROUTE = "welcome"
        const val AGE_ROUTE = "age"
        const val GENDER_ROUTE = "gender"
        const val HEIGHT_ROUTE = "height"
        const val WEIGHT_ROUTE = "weight"
        const val NUTRIENT_GOAL_ROUTE = "nutrient"
        const val ACTIVITY_ROUTE = "activity"
        const val GOAL_ROUTE = "goal"
        const val TRACKER_OVERVIEW_ROUTE = "tracker_overview"
        const val SEARCH_ROUTE = "search"
    }
}