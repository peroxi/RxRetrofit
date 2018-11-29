package com.rxretro.model.api

import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import com.rxretro.model.retrofit.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ApiFacade {

    fun fetchContributorsListApi(user: String?, repoName: String?): Observable<List<Contributor>> {
        return RetrofitHelper.githubAPI.listRepoContributors(user, repoName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchRepos(user: String): Observable<List<Repository>> {
        return RetrofitHelper.githubAPI.listRepos(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
