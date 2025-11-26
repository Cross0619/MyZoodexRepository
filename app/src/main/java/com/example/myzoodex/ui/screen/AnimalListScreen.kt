package com.example.myzoodex.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myzoodex.R
import com.example.myzoodex.model.Animal
import com.example.myzoodex.model.SortType

// 一覧画面の本体
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(
    animals: List<Animal>,
    searchText: String,                    // 受け取る変数が変わりました
    onSearchTextChange: (String) -> Unit,  // 受け取る関数が変わりました
    sortType: SortType,                    // ソート状態
    onSortTypeChange: (SortType) -> Unit,  // ソート変更時の処理
    onAnimalClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // ドロップダウンメニューの開閉状態
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MyZoodex 🦁") },
                actions = {
                    // ソートボタン
                    IconButton(onClick = { showMenu = true }) {
                        // アイコン (Sortがない場合は List などを代用)
                        Icon(Icons.Filled.List, contentDescription = "ソート")
                    }
                    // ドロップダウンメニュー
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        // Enumの全種類をメニューに表示
                        SortType.values().forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    // 選択中の項目は太字にしたり色を変えたり
                                    val isSelected = type == sortType
                                    Text(
                                        text = type.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    onSortTypeChange(type)
                                    showMenu = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 検索バー (引数が変わったので修正)
            MySearchBar(
                query = searchText,
                onQueryChange = onSearchTextChange
            )

            // リスト表示
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animals) { animal ->
                    AnimalCard(animal, onAnimalClick)
                }
            }
        }
    }
}

// 動物リストカード（クリック処理を追加）
@Composable
fun AnimalCard(
    animal: Animal,
    onAnimalClick: (Int) -> Unit // クリック処理を受け取る
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAnimalClick(animal.id) } // クリックされたら onAnimalClick を実行
    ) {
        Row(
            // (レイアウトのコードは省略、前回と同じ)
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 画像の表示
            Image(
                painter = painterResource(id = getAnimalImageRes(animal.id)),
                contentDescription = animal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = animal.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                }
                Text(text = "★".repeat(animal.popularity), color = MaterialTheme.colorScheme.primary)
                Text(
                    text = "${animal.area} / ${animal.terrain}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "分類:${animal.order}目 / ${animal.family}科",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


// 新しく追加する SearchBar の部品
// 名前を「MySearchBar」に変更しました
@Composable
fun MySearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("名前、目、科で検索") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


// IDから画像を動的に探す魔法の関数
@Composable
fun getAnimalImageRes(animalId: Int): Int {
    val context = LocalContext.current
    // "animal_ID" という名前のファイルを探しにいく
    val imageResId = context.resources.getIdentifier(
        "animal_$animalId", // 探す名前 (例: animal_100)
        "drawable",         // 探す場所
        context.packageName // このアプリのパッケージ内
    )

    // もし画像が見つかったらそのIDを、見つからなかったら(まだ用意してないなど)安全のためにanimal_1を返す
    return if (imageResId != 0) imageResId else R.drawable.animal_1
}


