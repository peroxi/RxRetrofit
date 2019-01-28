package com.rxretro.model.api

import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import com.rxretro.model.retrofit.RetrofitHelper
import com.rxretro.model.usecases.entity.ContributorsResponse
import com.rxretro.model.usecases.entity.RepositoryResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object ApiFacade {

    fun fetchContributorsListApi(user: String?, repoName: String?): Deferred<ContributorsResponse> {
        return GlobalScope.async(Dispatchers.IO) {
            var contributors = listOf<Contributor>()
            var errorMessage: String? = null
            try {
                val response = RetrofitHelper.githubAPI.listRepoContributors(user, repoName).await()
                response.run {
                    if (isSuccessful) {
                        contributors = body() ?: listOf()
                    } else {
                        errorMessage = message()
                    }
                }
                ContributorsResponse(contributors, errorMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                ContributorsResponse(contributors, errorMessage)
            }
        }
    }

    fun fetchRepos(user: String): Deferred<RepositoryResponse> {
        return GlobalScope.async(Dispatchers.IO) {
            var repositories = listOf<Repository?>()
            var errorMessage: String? = null
            try {
                val response = RetrofitHelper.githubAPI.listRepos(user).await()
                response.run {
                    if (isSuccessful) {
                        repositories = body() ?: listOf()
                    } else {
                        errorMessage = message()
                    }
                }
                RepositoryResponse(repositories, errorMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                RepositoryResponse(repositories, errorMessage)
            }
        }
    }
}
