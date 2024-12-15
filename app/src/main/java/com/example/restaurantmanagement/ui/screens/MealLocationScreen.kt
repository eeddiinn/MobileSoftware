package com.example.restaurantmanagement.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagement.R

@Composable
fun MealLocationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 설정
    ) {
        // 상단 회색 칸
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "식사 장소",
                color = Color.Black,
                fontSize = 22.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 장소 선택 카드
        LocationCard("상록원 2층", R.drawable.sangrok_2, navController)
        LocationCard("상록원 3층", R.drawable.sangrok_3, navController)
        LocationCard("기숙사 식당", R.drawable.dormitory, navController)

        Spacer(modifier = Modifier.weight(1f))

        // "식사 분석하기" 버튼
        Button(
            onClick = { navController.navigate("meal_analysis_screen") },
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
            Image(
                painter = painterResource(id = R.drawable.meal),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 8.dp)
            )

            Text(text = "식사 분석", fontSize = 16.sp)

        }
    }
}


@Composable
fun LocationCard(location: String, imageRes: Int, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 사진
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = location,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // 버튼
        Button(
            onClick = { navController.navigate("meal_screen/$location") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(text = location, color = Color.Black)
        }
    }
}
