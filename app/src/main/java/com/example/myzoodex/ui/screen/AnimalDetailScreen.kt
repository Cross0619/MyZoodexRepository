package com.example.myzoodex.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myzoodex.model.Animal

// ページ送り機能付きの詳細画面（親）
@OptIn(ExperimentalFoundationApi::class) // Pagerを使うためのおまじない
@Composable
fun AnimalDetailScreen(
    animals: List<Animal>,
    initialAnimalId: Int,
    onBackClick: () -> Unit
) {
    // 最初にどのページ(何番目の動物)を開くか計算
    // データ上のIDと、リストの順番(0から始まる)を合わせる
    val initialPage = animals.indexOfFirst { it.id == initialAnimalId }.takeIf { it >= 0 } ?: 0

    // ページの状態を管理するもの
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { animals.size }
    )

    // 横スワイプの箱を作る
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { pageIndex ->
        // pageIndex番目の動物データを取得
        val animal = animals[pageIndex]

        // 実際の表示はこの関数にお任せ
        AnimalDetailContent(animal = animal, onBackClick = onBackClick)
    }
}

// 今までの詳細画面の中身（子）
// 名前を AnimalDetailContent に変更しました
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailContent(
    animal: Animal,
    onBackClick: () -> Unit
) {
    // ドロップダウンメニューの開閉状態
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("No.${animal.id} ${animal.name}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. メイン画像
                Image(
                    painter = painterResource(id = getAnimalImageRes(animal.id)),
                    contentDescription = animal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )

                // 2. 情報エリア
                Column(modifier = Modifier.padding(24.dp)) {
                    // 名前と人気度
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = animal.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    // 人気度の★ (左寄せ修正済み)
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "★".repeat(animal.popularity),
                            color = Color(0xFFFFD700),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    // 3. スペック表
                    SectionTitle(title = "基本データ")
                    InfoRow(label = "分類", value = "${animal.order}目 / ${animal.family}科")
                    InfoRow(label = "大きさ", value = animal.size)
                    InfoRow(label = "生息地", value = animal.distribution)
                    InfoRow(label = "エリア", value = "${animal.area} (${animal.terrain})")

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle(title = "生態・飼育")
                    InfoRow(label = "エサ", value = animal.food)
                    InfoRow(label = "飼育数", value = "${animal.count}頭")

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle(title = "説明文")
                    Text( // Text() で囲んで画面に描画する
                        text = animal.detailedDescription,
                        style = MaterialTheme.typography.bodyLarge, // 読みやすいフォントサイズを指定
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

// 見出し用のパーツ
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// データ行のパーツ
@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp), // ラベル幅
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


