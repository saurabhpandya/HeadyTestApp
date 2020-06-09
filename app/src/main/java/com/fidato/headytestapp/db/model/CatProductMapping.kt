package com.fidato.headytestapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fidato.headytestapp.utils.DateConverter
import java.io.Serializable
import java.util.*

@Entity
@TypeConverters(DateConverter::class)
data class CatProductMapping(
    @PrimaryKey
    var id: Int = 0,
    var cat_id: Int = 0,
    var name: String = "",
    var date_added: Date? = null,
    var taxName: String = "",
    var taxValue: Double = 0.0
) : Serializable