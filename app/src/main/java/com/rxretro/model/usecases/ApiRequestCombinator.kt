package com.rxretro.model.usecases

import android.arch.persistence.room.Room
import android.content.Context
import com.rxretro.model.api.ApiFacade
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers

object ApiRequestCombinator {

    fun fetchContributorsList(user: String, applicationContext: Context): Observable<List<Contributor>> {
        return ApiFacade.fetchRepos(user)
            .flatMapIterable { it }
            .flatMap { t: Repository? ->
                Observable.create<Repository?> {
                    updateRepositoryDB(it, applicationContext, t)
                }.subscribeOn(Schedulers.io())
            }
            .flatMap { repo -> ApiFacade.fetchContributorsListApi(user, repo.name) }

    }

    private fun updateRepositoryDB(it: ObservableEmitter<Repository?>, applicationContext: Context, t: Repository?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        t?.let {
            db.repositoryDao().deleteRepository()
            db.repositoryDao().insertRepositories(listOf(it))
        }
        if (t != null) {
            it.onNext(t)
        }
        it.onComplete()
    }
}
