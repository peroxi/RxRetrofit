package com.rxretro.model.usecases

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object EventDataFlattener {
    fun fetchContributors(user: String, applicationContext: Context): Observable<Contributor> {
        return Observable.create<Contributor> { emitter ->
            ApiRequestCombinator.fetchContributorsList(user, applicationContext)
                .flatMapIterable { it }
                .distinct()
                .doOnComplete { emitter.onComplete() }
                .subscribe { GlobalScope.launch { updateDatabaseContributors(it, applicationContext, emitter) } }
        }
    }

    private fun updateDatabaseContributors(it: Contributor,
                                           applicationContext: Context,
                                           emitter: ObservableEmitter<Contributor>) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        db.repositoryDao().insertContributors(it)
        Log.i("Cont First put to db: ", it.name)
        emitter.onNext(it)
    }
}
