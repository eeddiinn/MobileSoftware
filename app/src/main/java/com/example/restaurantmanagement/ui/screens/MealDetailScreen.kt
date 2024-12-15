package com.example.restaurantmanagement.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.restaurantmanagement.MealViewModel

@Composable
fun MealDetailScreen(meal: MealViewModel.Meal, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // 스크롤 가능 설정
    ) {
        // 상단 회색 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        // 상단 회색 칸 아래 여백
        Spacer(modifier = Modifier.height(16.dp))

        // 이미지
        meal.imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Meal Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(222.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // 이미지와 입력 칸 사이의 여백
        Spacer(modifier = Modifier.height(12.dp))

        // 음식 이름
        DetailItem(label = "음식", value = meal.name)

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 반찬 이름
        DetailItem(label = "반찬", value = meal.sideDishes)

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 비용
        DetailItem(label = "비용", value = "${meal.cost}원")

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 날짜
        DetailItem(label = "날짜", value = meal.date)

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 칼로리
        DetailItem(label = "칼로리", value = "${meal.calories} kcal")

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 리뷰
        Text(
            text = "리뷰",
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        Text(
            text = meal.review,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(60.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        // 입력 필드 간 간격
        Spacer(modifier = Modifier.height(10.dp))

        // 종류
        Text(
            text = "종류",
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val mealTypes = listOf("조식", "중식", "석식", "간식/음료")
            mealTypes.forEach { type ->
                Button(
                    onClick = { /* 클릭 비활성화 */ },
                    modifier = Modifier
                        .width(if (type == "간식/음료") 100.dp else 80.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == meal.type) Color(0xFF3D4857) else Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = type, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }
}

