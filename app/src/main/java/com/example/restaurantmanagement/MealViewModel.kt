package com.example.restaurantmanagement

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MealViewModel : ViewModel() {
    // 장소별 데이터를 저장하는 Map
    private val mealData = mutableMapOf(
        "상록원 2층" to mutableStateOf<List<Meal>>(emptyList()),
        "상록원 3층" to mutableStateOf<List<Meal>>(emptyList()),
        "기숙사 식당" to mutableStateOf<List<Meal>>(emptyList())
    )

    // 최근 1달 간의 식사를 필터링
    fun getMealsInLastMonth(): List<Meal> {
        val oneMonthAgo = Calendar.getInstance()
        oneMonthAgo.add(Calendar.MONTH, -1) 
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return mealData.flatMap { entry ->
            entry.value.value.filter { meal ->
                val mealDate = dateFormatter.parse(meal.date)
                mealDate != null && mealDate.after(oneMonthAgo.time)
            }
        }
    }
    // 칼로리 총합 계산
    fun getTotalCaloriesInLastMonth(): Int {
        return getMealsInLastMonth().sumBy { it.calories }
    }
    // 식사 비용 종류별 분석
    fun getCostByMealType(): Map<String, Int> {
        val costByType = mutableMapOf<String, Int>()

        getMealsInLastMonth().forEach { meal ->
            val type = meal.type
            val cost = meal.cost
            costByType[type] = costByType.getOrDefault(type, 0) + cost
        }

        return costByType
    }

    // 특정 장소의 데이터를 가져오기
    fun getMealsForLocation(location: String) = mealData[location]?.value ?: emptyList()

    // 특정 장소에 식사 데이터 추가
    fun addMeal(location: String, meal: Meal) {
        val updatedMeals = mealData[location]?.value?.toMutableList() ?: mutableListOf()
        updatedMeals.add(meal)  // meal 추가
        mealData[location]?.value = updatedMeals // Meal 객체에 칼로리 포함 새로운 리스트 저장
    }

    data class Meal(
        val name: String,
        val sideDishes: String,
        val date: String,
        val type: String,
        val review: String,
        val imageUri: String?,
        val calories: Int,
        val cost:Int
    )
}

