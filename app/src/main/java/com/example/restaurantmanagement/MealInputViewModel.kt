package com.example.restaurantmanagement

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MealInputViewModel : ViewModel() {
    val image = mutableStateOf<String?>(null)
    val name = mutableStateOf("")
    val cost = mutableStateOf("")
    val date = mutableStateOf("")
    val review = mutableStateOf("")
    val selectedMealType = mutableStateOf<String?>(null)
    val sideDishes = mutableStateOf("")

}
