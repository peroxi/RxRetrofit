package com.rxretro.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "contributor")
data class Contributor(
    @PrimaryKey
    @ColumnInfo(name = "login")
    @SerializedName("login") val name: String,
    @ColumnInfo(name = "contributions") val contributions: Int
) {

    override fun toString(): String {
        return "Contributer [name=" + name + ", contributions=" + contributions + "]"
    }
}

