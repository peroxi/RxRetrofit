package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.rxretro.model.api.ApiFacade
import com.rxretro.model.entity.Contributor
import com.rxretro.model.usecases.DBInteractor.fetchFromDB
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object UseCaseFacade {

    @SuppressLint("CheckResult")
    fun fetchContributorsOfUsersRepositoriesAsync(user: String, applicationContext: Context): Deferred<List<String>> {
        val contributorsGeneralList: MutableList<String> = mutableListOf()
        return GlobalScope.async(Dispatchers.IO) {
            val reposResponse = ApiFacade.fetchRepos(user)
            reposResponse.run {
                if (reposResponse.errorMessage == null) {
                    reposResponse.repositoriesList.iterator().forEach {
                        DBInteractor.updateRepositoryDB(applicationContext, it)
                        val contributorsResponse = ApiFacade.fetchContributorsListApi(user, it?.name)
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
                    fetchFromDB(applicationContext, user)
                } else {
                    fetchFromDB(applicationContext, user)
                }
            }
        }
    }
}
