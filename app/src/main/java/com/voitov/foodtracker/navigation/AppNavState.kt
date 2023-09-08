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
    data class TrackableFoodManager(val managerRoute: String): AppNavState(TRACKABLE_FOOD_MANAGER_ROUTE) {
        companion object {
            fun createRoute(
                mealType: String,
                year: Int,
                month: Int,
                dayOfWeek: Int,
            ): String {
                return TRACKABLE_FOOD_MANAGER_ROUTE
                    .replace("{$MEAL_TYPE_KEY}", mealType)
                    .replace("{$YEAR_KEY}", year.toString())
                    .replace("{$MONTH_KEY}", month.toString())
                    .replace("{$DAY_OF_WEEK_KEY}", dayOfWeek.toString())
            }

            const val MEAL_TYPE_KEY = "meal_type"
            const val YEAR_KEY = "year"
            const val MONTH_KEY = "month"
            const val DAY_OF_WEEK_KEY = "day_of_week"
        }
    }
    data class Search(val searchRoute: String) : AppNavState(searchRoute) {
        companion object {
            fun createRoute(
                mealType: String,
                year: Int,
                month: Int,
                dayOfWeek: Int,
//                hours: Int,
//                minutes: Int
            ): String {
                return SEARCH_ROUTE
                    .replace("{$MEAL_TYPE_KEY}", mealType)
                    .replace("{$YEAR_KEY}", year.toString())
                    .replace("{$MONTH_KEY}", month.toString())
                    .replace("{$DAY_OF_WEEK_KEY}", dayOfWeek.toString())
//                    .replace(HOURS_KEY, hours.toString())
//                    .replace(MINUTES_KEY, minutes.toString())
            }

            const val MEAL_TYPE_KEY = "meal_type"
            const val YEAR_KEY = "year"
            const val MONTH_KEY = "month"
            const val DAY_OF_WEEK_KEY = "day_of_week"
//            const val HOURS_KEY = "hours"
//            const val MINUTES_KEY = "minutes"
        }
    }
    object CustomFoodManager: AppNavState(CUSTOM_FOOD_ROUTE)

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
        const val CUSTOM_FOOD_ROUTE = "custom_food"
        const val TRACKABLE_FOOD_MANAGER_ROUTE = "trackable_manager/{${TrackableFoodManager.MEAL_TYPE_KEY}}/{${TrackableFoodManager.YEAR_KEY}}/{${TrackableFoodManager.MONTH_KEY}}/{${TrackableFoodManager.DAY_OF_WEEK_KEY}}"
        const val SEARCH_ROUTE =
            "search/{${Search.MEAL_TYPE_KEY}}/{${Search.YEAR_KEY}}/{${Search.MONTH_KEY}}/{${Search.DAY_OF_WEEK_KEY}}" ///{${Search.HOURS_KEY}}/{${Search.MINUTES_KEY}}
    }
}