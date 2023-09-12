package com.voitov.foodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

    private var keepSplashScreenVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreenVisible
            }
        }
        setContent {
            FoodTrackerTheme {
                AppNavGraph(
                    onScreenIsReady = { isReady ->
                      keepSplashScreenVisible = !isReady
                    },
                    startDestination = getStartDestination(),
                )
            }
        }
    }

    private fun getStartDestination(): String {
        val needOnboarding = keyValueStorage.loadWhetherOnboardingIsRequired()
        return if (needOnboarding) {
            AppNavState.Welcome.route
        } else {
            AppNavState.TrackerOverview.route
        }
    }
}

