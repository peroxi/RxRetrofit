package com.rxretro.model.usecases

import com.rxretro.model.api.ApiFacade
import com.rxretro.model.entity.Contributor
import io.reactivex.Observable

object ApiRequestCombinator {

    fun fetchContributorsList(user: String): Observable<List<Contributor>> {
        return ApiFacade.fetchRepos(user)
            .flatMapIterable { it }
            .flatMap { repo -> ApiFacade.fetchContributorsListApi(user, repo.name) }
    }
}
