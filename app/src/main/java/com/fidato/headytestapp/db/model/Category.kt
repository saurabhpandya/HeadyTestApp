package com.fidato.headytestapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(

//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0,
    @PrimaryKey
    var catId: Int = 0,

    var name: String = "",

    var parentName: String = "",

    var parentId: Int = 0
)