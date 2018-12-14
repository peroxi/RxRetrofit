package com.rxretro.model.dao

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository

object DBRepository {
    @SuppressLint("CheckResult")
    fun fetchFromDB(applicationContext: Context, user: String): List<String> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        return db.repositoryDao().selectContributorsOfUsersRepositories(user)
    }

    fun updateDatabaseContributors(
        it: List<Contributor>,
        applicationContext: Context
    ) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        db.repositoryDao().insertContributors(it)
        it.iterator().forEach {
            Log.i("Cont First put to db: ", it.name)
        }
    }

    fun updateRepositoryDB(applicationContext: Context, t: List<Repository?>?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        t?.let {
            db.repositoryDao().insertRepositories(it)
        }
    }
}