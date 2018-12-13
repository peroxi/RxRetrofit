package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import com.rxretro.model.retrofit.RetrofitHelper
import kotlinx.coroutines.*

object ApiRequestCombinator {

    fun fetchContributorsList(user: String, applicationContext: Context): Deferred<List<String>> {
        return GlobalScope.async(Dispatchers.IO) {
            val contributors = HashSet<Contributor>()
            val response = RetrofitHelper.githubAPI.listRepos(user).await()
            if (response.isSuccessful) {
                updateRepositoryDB(applicationContext, response.body())
                response.body()?.iterator()?.forEach {
                    contributors.addAll(
                        RetrofitHelper.githubAPI.listRepoContributors(user, it?.name).await().body() ?: listOf()
                    )
                }
            }
            updateDatabaseContributors(contributors.toList(), applicationContext)
            fetchFromDB(applicationContext, user)
        }

    }

    @SuppressLint("CheckResult")
    private fun fetchFromDB(applicationContext: Context, user: String): List<String> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        return db.repositoryDao().selectContributorsOfUsersRepositories(user)
    }

    private fun updateDatabaseContributors(
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

    private fun updateRepositoryDB(applicationContext: Context, t: List<Repository?>?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        t?.let {
            db.repositoryDao().insertRepositories(it)
        }
    }
}
