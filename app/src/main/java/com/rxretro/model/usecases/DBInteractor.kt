package com.rxretro.model.usecases

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object DBInteractor {

    suspend fun updateDatabaseContributors(
        it: Contributor,
        applicationContext: Context
    ) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        GlobalScope.async {
            db.repositoryDao().insertContributors(it)
        }.await()
        Log.i("Cont First put to db: ", it.name)
    }

    suspend fun updateRepositoryDB(applicationContext: Context, t: Repository?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        GlobalScope.async {
            t?.let {
                db.repositoryDao().deleteRepository()
                db.repositoryDao().insertRepositories(listOf(it))
            }
        }.await()
    }

    suspend fun fetchFromDB(applicationContext: Context, user: String): List<String> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        return GlobalScope.async {
            db.repositoryDao().selectContributorsOfUsersRepositories(user)
        }.await()
    }
}