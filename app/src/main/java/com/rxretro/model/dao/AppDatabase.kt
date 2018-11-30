package com.rxretro.model.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository

@Database(entities = arrayOf(Repository::class, Contributor::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoryDao() : RepositoryDao
}
