package com.rxretro.model.usecases

import android.content.Context
import com.rxretro.model.dao.DBRepository.fetchFromDB
import com.rxretro.model.dao.DBRepository.updateDatabaseContributors
import com.rxretro.model.dao.DBRepository.updateRepositoryDB
import com.rxretro.model.entity.Contributor
import com.rxretro.model.retrofit.RetrofitHelper
import kotlinx.coroutines.*

object ApiRequestUsecase {

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
}
