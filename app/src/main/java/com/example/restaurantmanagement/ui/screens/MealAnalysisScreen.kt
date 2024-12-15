package com.example.restaurantmanagement.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagement.MealViewModel
import com.example.restaurantmanagement.R

@Composable
fun MealAnalysisScreen(viewModel: MealViewModel, navController: NavController) {
    val totalCalories = viewModel.getTotalCaloriesInLastMonth() ?: 0
    val meals = viewModel.getMealsInLastMonth().orEmpty()

    val costByType = remember(meals) {
        meals.groupBy { it.type }.mapValues { (_, mealsByType) ->
            mealsByType.sumOf { it.cost }
        }
    }
    val totalCost = costByType.values.sum()

    val mealsByType = meals.groupBy { it.type } // 식사 데이터를 종류별로 그룹화

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = "식사 분석",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExpandableCard(
                title = "최근 1달 간의 칼로리 총량",
                value = "$totalCalories Kcal",
                iconRes = R.drawable.kcal,
                details = meals.map { "${it.name} : ${it.calories} Kcal" }
            )

            ExpandableCard(
                title = "최근 1달 간의 총 식비",
                value = "$totalCost 원",
                iconRes = R.drawable.money,
                details = meals.map { "${it.name} : ${it.cost} 원" }
            )

            ExpandableCardWithGraphAndDetails(
                title = "최근 1달 간 종류 별 식비",
                costByType = costByType,
                mealsByType = mealsByType
            )
        }
    }
}

@Composable
fun FixedBarChartWithLabels(costByType: Map<String, Int>) {
    val maxCost = costByType.values.maxOrNull() ?: 1
    val barColors = listOf(
        "조식" to Color(0xFFFFE1A1),
        "중식" to Color(0xFF83BDF5),
        "석식" to Color(0xFFA9E5AA),
        "간식/음료" to Color(0xFFFFA5C6)
    ).toMap()

    val mealTypes = listOf("조식", "중식", "석식", "간식/음료")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        mealTypes.forEach { type ->
            val cost = costByType[type] ?: 0
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = type,
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                // 막대 그래프
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth(0.7f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(cost.toFloat() / maxCost) // 비례에 따라 길이 조정
                            .background(barColors[type] ?: Color.Gray, RoundedCornerShape(4.dp))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                // 막대 그래프 끝에 가격 표시
                Text(
                    text = "${cost}원",
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExpandableCard(title: String, value: String, iconRes: Int, details: List<String>) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isExpanded) 250.dp else 120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded) {
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    details.forEach {
                        Text(text = it, fontSize = 14.sp, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (isExpanded) "▲ 닫기 ▲" else "▼ 상세 ▼",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}

@Composable
fun ExpandableCardWithGraphAndDetails(
    title: String,
    costByType: Map<String, Int>,
    mealsByType: Map<String, List<MealViewModel.Meal>>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.meal),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 막대 그래프
            FixedBarChartWithLabels(costByType)

            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded) {
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                val mealTypes = listOf("조식", "중식", "석식", "간식/음료")
                mealTypes.forEach { type ->
                    val meals = mealsByType[type] ?: emptyList()

                    // 종류별 타이틀
                    Text(
                        text = type,
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    if (meals.isEmpty()) {
                        Text(
                            text = "등록된 식사가 없습니다.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    } else {
                        meals.forEach { meal ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${meal.name} : ${meal.cost}원",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (isExpanded) "▲ 닫기 ▲" else "▼ 상세 ▼",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}


