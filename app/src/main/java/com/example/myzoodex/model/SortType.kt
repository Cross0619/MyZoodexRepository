package com.example.myzoodex.model

enum class SortType(val label: String) {
    ID_ASC("No. 昇順 (1→200)"),
    ID_DESC("No. 降順 (200→1)"),
    POPULARITY_DESC("人気が高い順"),
    POPULARITY_ASC("人気が低い順"),
    NAME_ASC("名前順 (ア→ン)"),
    NAME_DESC("名前順 (ン→ア)")
}
