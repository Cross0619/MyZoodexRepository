package com.example.myzoodex.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myzoodex.R
import kotlinx.coroutines.delay

// 一番下に追加してください

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // 起動したら2秒(2000ミリ秒)待ってから、次の画面へ進むタイマー
    // import androidx.compose.runtime.LaunchedEffect
    // import kotlinx.coroutines.delay
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        onTimeout()
    }

    // 画面レイアウト
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // 背景は白
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            // 真ん中より「少し上」にするために、Y座標を -50dp ずらす
            modifier = Modifier.offset(y = (-50).dp)
        ) {
            // ゾウのアイコン
            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp), // アイコンのサイズ（お好みで調整）
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // タイトルテキスト
            Text(
                text = "MyZoodex",
                fontSize = 40.sp, // 大きめの文字
                fontWeight = FontWeight.Bold,
                // ゾウの色に合わせたラベンダー色（もし違ったらここを調整！）
                color = Color(0xFFD69EF0),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}


