package com.rxretro.model.usecases

import android.content.Context
import com.rxretro.model.entity.Contributor
import io.reactivex.Observable

object EventDataFlattener {
    fun fetchContributors(user: String, applicationContext: Context): Observable<Contributor> {
        return Observable.create<Contributor> { emitter ->
            ApiRequestCombinator.fetchContributorsList(user, applicationContext)
                .flatMapIterable { it }
                .subscribe { emitter.onNext(it) }
        }
    }
}
