package com.voitov.foodtracker

import com.voitov.foodtracker.ui.theme.FoodTrackerTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.foodtracker.navigation.AppNavGraph
import com.voitov.foodtracker.navigation.AppNavState
import com.voitov.onboarding_presentation.welcome.HelloScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodTrackerTheme {
                AppNavGraph(startDestination = AppNavState.WELCOME_ROUTE)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodTrackerTheme {
        HelloScreen {

        }
    }
}