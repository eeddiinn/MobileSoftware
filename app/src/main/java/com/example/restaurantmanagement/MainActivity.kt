package com.example.restaurantmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagement.ui.screens.MealAnalysisScreen
import com.example.restaurantmanagement.ui.screens.MealDetailScreen
import com.example.restaurantmanagement.ui.screens.MealInputScreen
import com.example.restaurantmanagement.ui.screens.MealLocationScreen
import com.example.restaurantmanagement.ui.screens.MealScreen
import com.example.restaurantmanagement.ui.theme.RestaurantManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantManagementTheme {
                val navController = rememberNavController()
                val mealViewModel: MealViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "meal_location_screen"
                ) {
                    composable("meal_location_screen") {
                        MealLocationScreen(navController)
                    }
                    composable("meal_screen/{location}") { backStackEntry ->
                        val location = backStackEntry.arguments?.getString("location") ?: "알 수 없음"
                        MealScreen(location = location, navController = navController, viewModel = mealViewModel)
                    }
                    composable("meal_input_screen/{location}") { backStackEntry ->
                        val location = backStackEntry.arguments?.getString("location") ?: "알 수 없음"
                        MealInputScreen(location = location, mealViewModel = mealViewModel, navController = navController)
                    }
                    composable("meal_analysis_screen"){
                        MealAnalysisScreen(viewModel = mealViewModel,navController = navController)
                    }
                    composable("meal_detail_screen/{mealName}") { backStackEntry ->
                        val mealName = backStackEntry.arguments?.getString("mealName")
                        val meal = mealViewModel.getMealsInLastMonth().find { it.name == mealName }
                        if (meal != null) {
                            MealDetailScreen(meal = meal, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

