package com.rxretro.model.usecases

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository

object DBInteractor {

    fun updateDatabaseContributors(
        it: Contributor,
        applicationContext: Context
    ) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        db.repositoryDao().insertContributors(it)
        Log.i("Cont First put to db: ", it.name)
    }

    fun updateRepositoryDB(applicationContext: Context, t: Repository?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        t?.let {
            db.repositoryDao().deleteRepository()
            db.repositoryDao().insertRepositories(listOf(it))
        }
    }

    fun fetchFromDB(applicationContext: Context, user: String): List<String> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        return db.repositoryDao().selectContributorsOfUsersRepositories(user)
    }
}