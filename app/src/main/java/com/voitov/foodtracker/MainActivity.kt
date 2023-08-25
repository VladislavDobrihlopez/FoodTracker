package com.voitov.foodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.foodtracker.navigation.AppNavGraph
import com.voitov.foodtracker.navigation.AppNavState
import com.voitov.foodtracker.ui.theme.FoodTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var keyValueStorage: UserInfoKeyValueStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodTrackerTheme {
                AppNavGraph(startDestination = getStartDestination())
            }
        }
    }

    private fun getStartDestination() =
        when (keyValueStorage.loadWhetherOnboardingIsRequired()) {
            true -> AppNavState.Welcome.route
            false -> AppNavState.TrackerOverview.route
        }
}

