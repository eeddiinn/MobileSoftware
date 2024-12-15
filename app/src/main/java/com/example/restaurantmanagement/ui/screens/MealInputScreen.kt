package com.example.restaurantmanagement.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.restaurantmanagement.MealInputViewModel
import com.example.restaurantmanagement.MealViewModel

@Composable
fun MealInputScreen(
    location: String,
    mealViewModel: MealViewModel,
    inputViewModel: MealInputViewModel = viewModel(),
    navController: NavController
) {
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // 식사 종류에 따른 칼로리 랜덤 생성(상대적으로 조식은 낮은 칼로리,석식은 높은 칼로리 범위로 랜덤 값 지정)
    fun getRandomCalories(mealType: String?): Int {
        return when (mealType) {
            "조식" -> (300..400).random()
            "중식" -> (700..900).random()
            "석식" -> (800..1000).random()
            "간식/음료" -> (100..250).random()
            else -> 0
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        inputViewModel.image.value = uri?.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize()
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

        Spacer(modifier = Modifier.height(16.dp))

        // 아래 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()), // 스크롤 가능하도록 설정
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 사진 업로드 UI
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") }, // 갤러리 열기
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri), // 선택된 이미지 표시
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("+", fontSize = 48.sp, color = Color.Gray)
                }
            }

            // 공통 TextField 색상 스타일
            val textFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray,
                focusedTextColor = Color.DarkGray,
                unfocusedTextColor = Color.DarkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )

            // 음식 이름 입력 필드
            TextField(
                value = inputViewModel.name.value,
                onValueChange = { inputViewModel.name.value = it },
                label = { Text("음식 이름(음료인 경우 음료수 이름)", color = Color.DarkGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                colors = textFieldColors
            )

            // 반찬 이름 입력 필드
            TextField(
                value = inputViewModel.sideDishes.value,
                onValueChange = { inputViewModel.sideDishes.value = it },
                label = { Text("반찬 이름", color = Color.DarkGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                colors = textFieldColors
            )

            // 비용 입력 필드
            TextField(
                value = inputViewModel.cost.value,
                onValueChange = { inputViewModel.cost.value = it },
                label = { Text("비용", color = Color.DarkGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                colors = textFieldColors
            )

            // 날짜 선택
            Text(
                text = if (inputViewModel.date.value.isEmpty()) "날짜 선택" else inputViewModel.date.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDatePickerVisible = true } // 날짜 클릭 시 DatePicker 나타내기
                    .padding(16.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            if (isDatePickerVisible) {
                DatePicker(onDateSelected = { date ->
                    inputViewModel.date.value = date
                    isDatePickerVisible = false
                }, onDismiss = {
                    isDatePickerVisible = false
                })
            }

            // 리뷰 입력 필드
            TextField(
                value = inputViewModel.review.value,
                onValueChange = { inputViewModel.review.value = it },
                label = { Text("리뷰", color = Color.DarkGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                singleLine = false,
                maxLines = 4,
                colors = textFieldColors
            )

            // 종류 선택 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val mealTypes = listOf("조식", "중식", "석식", "간식/음료")
                val selectedColor = Color(0xFF3D4857)
                val defaultColor = Color.Gray

                mealTypes.forEach { type ->
                    Button(
                        onClick = {
                            inputViewModel.selectedMealType.value =
                                if (inputViewModel.selectedMealType.value == type) null else type
                        },
                        modifier = Modifier
                            .width(if (type == "간식/음료") 100.dp else 80.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputViewModel.selectedMealType.value == type) selectedColor else defaultColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = type, fontSize = 12.sp)
                    }
                }
            }

            // 저장 버튼
            Button(
                onClick = {
                    // 임의로 칼로리 값을 설정하여 Meal 객체를 생성
                    val calories = getRandomCalories(inputViewModel.selectedMealType.value)

                    // Meal 객체를 생성하여 MealViewModel에 전달
                    val newMeal = MealViewModel.Meal(
                        name = inputViewModel.name.value,
                        date = inputViewModel.date.value,
                        type = inputViewModel.selectedMealType.value ?: "기타",
                        review = inputViewModel.review.value,
                        imageUri = selectedImageUri?.toString(),
                        calories = calories,
                        sideDishes = inputViewModel.sideDishes.value,
                        cost = inputViewModel.cost.value.toIntOrNull() ?:0
                    )
                    mealViewModel.addMeal(location, newMeal) // Meal 객체 전달
                    Toast.makeText(
                        context,
                        "식사가 저장되었습니다!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("저장")
            }
        }
    }
}



// 날짜 선택기
@Composable
fun DatePicker(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH)
    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

    androidx.compose.runtime.LaunchedEffect(Unit) {
        android.app.DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            onDateSelected(formattedDate)
        }, year, month, day).apply {
            setOnDismissListener { onDismiss() }
        }.show()
    }
}

