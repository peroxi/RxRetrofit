package com.rxretro.model.api

import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import com.rxretro.model.retrofit.RetrofitHelper
import com.rxretro.model.usecases.entity.ContributorsResponse
import com.rxretro.model.usecases.entity.RepositoryResponse

object ApiFacade {

    suspend fun fetchContributorsListApi(user: String?, repoName: String?): ContributorsResponse {
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
                return ContributorsResponse(contributors, listOf(), errorMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                return ContributorsResponse(contributors, listOf(), errorMessage)
            }
    }

    suspend fun fetchRepos(user: String): RepositoryResponse {
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
                return RepositoryResponse(repositories, errorMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                return RepositoryResponse(repositories, errorMessage)
            }
    }
}
