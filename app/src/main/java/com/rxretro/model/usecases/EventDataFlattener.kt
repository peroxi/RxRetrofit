package com.rxretro.model.usecases

import com.rxretro.model.entity.Contributor
import io.reactivex.Observable

object EventDataFlattener {
    fun fetchContributors(user: String): Observable<Contributor> {
        return Observable.create<Contributor> { emitter ->
            ApiRequestCombinator.fetchContributorsList(user)
                .flatMapIterable { it }
                .subscribe { emitter.onNext(it) }
        }
    }
}
