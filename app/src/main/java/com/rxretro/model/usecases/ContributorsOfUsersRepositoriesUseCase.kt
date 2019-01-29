package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.rxretro.model.api.ApiFacade
import com.rxretro.model.entity.Contributor
import com.rxretro.model.usecases.DBInteractor.fetchFromDB
import com.rxretro.model.usecases.entity.ContributorsResponse
import com.rxretro.model.usecases.entity.RepositoryResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object ContributorsOfUsersRepositoriesUseCase {

    @SuppressLint("CheckResult")
    fun fetchContributorsOfUsersRepositoriesAsync(user: String, applicationContext: Context): Deferred<List<String>> {
        val contributorsGeneralList: MutableList<String> = mutableListOf()
        return GlobalScope.async(Dispatchers.IO) {
            val reposResponse = ApiFacade.fetchRepos(user)
            handleRepositoryResponseAsync(reposResponse, contributorsGeneralList, user, applicationContext)
        }
    }

    private suspend fun handleRepositoryResponseAsync(
        reposResponse: RepositoryResponse,
        contributorsGeneralList: MutableList<String>,
        user: String,
        applicationContext: Context
    ): List<String> {
        when (reposResponse.errorMessage) {
            null -> {
                reposResponse.repositoriesList.iterator().forEach {
                    DBInteractor.updateRepositoryDB(applicationContext, it)
                    val contributorsResponse = ApiFacade.fetchContributorsListApi(user, it?.name)
                    handleContributorsResponseAsync(
                        contributorsResponse,
                        contributorsGeneralList,
                        applicationContext
                    )
                }
            }
        }
        return fetchFromDB(applicationContext, user)
    }

    private suspend fun handleContributorsResponseAsync(
        contributorsResponse: ContributorsResponse,
        contributorsGeneralList: MutableList<String>,
        applicationContext: Context
    ) {
        contributorsResponse.run {
            if (errorMessage == null) {
                contributors.iterator().forEach { cont: Contributor ->
                    if (!contributorsGeneralList.contains(cont.name)) {
                        Log.i("Cont First updating Contributors DB starts for ", cont.name)
                        DBInteractor.updateDatabaseContributors(cont, applicationContext)
                        contributorsGeneralList.add(cont.name)
                        Log.i("Cont First update Contributors DB complete ", cont.name)
                    }
                }
            }
        }
    }
}
