package com.example.restaurantmanagement.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.restaurantmanagement.MealViewModel
import com.example.restaurantmanagement.R

@Composable
fun MealScreen(location: String, navController: NavController, viewModel: MealViewModel) {
    var selectedType by remember { mutableStateOf("전체") } // 선택된 타입 저장
    val meals = viewModel.getMealsForLocation(location)
        .filter { selectedType == "전체" || it.type == selectedType } // 타입에 따라 필터링
        .sortedByDescending { it.date } // 날짜 기준 내림차순 정렬

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 상단 회색 칸
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.LightGray)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "내 식사",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 26.dp)
                    )
                }
                Text(
                    text = "[$location]",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 버튼 리스트 (전체, 조식, 중식, 석식, 간식/음료)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val mealTypes = listOf("전체", "조식", "중식", "석식", "간식/음료")
            val selectedColor = Color(0xFF3D4857)
            val defaultColor = Color.Gray

            mealTypes.forEach { type ->
                Button(
                    onClick = { selectedType = type },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == selectedType) selectedColor else defaultColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = type,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        // 식사 목록을 2칸 그리드 형식으로 표시
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(meals) { meal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            navController.navigate("meal_detail_screen/${meal.name}")
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 이미지 표시
                        meal.imageUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Meal Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 이름 표시
                        Text(
                            text = meal.name,
                            fontSize = 16.sp,
                            color = Color.Black,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // 날짜 표시
                        Text(
                            text = meal.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // 화면 하단에 고정된 "식사 추가" 버튼
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("meal_input_screen/$location") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .height(48.dp)
                .width(160.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF5A6375),
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 아이콘 추가
                Image(
                    painter = painterResource(id = R.drawable.meal),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )

                // 텍스트
                Text(
                    text = "식사 추가",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}
