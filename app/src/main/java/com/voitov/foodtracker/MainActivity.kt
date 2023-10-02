package com.voitov.foodtracker

import android.os.Bundle
import android.util.Log
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
        Log.d(TAG, "onCreate()")

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

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        keepSplashScreenVisible = savedInstanceState.getBoolean(KEY_SPLASH_SCREEN_VISIBILITY)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        outState.putBoolean(KEY_SPLASH_SCREEN_VISIBILITY, keepSplashScreenVisible)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
    }

    private fun getStartDestination(): String {
        val needOnboarding = keyValueStorage.loadWhetherOnboardingIsRequired()
        return if (needOnboarding) {
            AppNavState.Welcome.route
        } else {
            AppNavState.TrackerOverview.route
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_SPLASH_SCREEN_VISIBILITY = "splash_screen"
    }
}

