package com.rxretro.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description")val description: String?
) {

    override fun toString(): String {
        return "Repository [name=" + name + ", description=" + description + "]"
    }
}
