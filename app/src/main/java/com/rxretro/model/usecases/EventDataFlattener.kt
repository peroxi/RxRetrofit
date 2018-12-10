package com.rxretro.model.usecases

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import com.rxretro.model.entity.Contributor
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object EventDataFlattener {
    fun fetchContributors(user: String, applicationContext: Context): Observable<Contributor> {
        var cont: Contributor? = null
        return Observable.create<Contributor> { emitter ->
            ApiRequestCombinator.fetchContributorsList(user, applicationContext)
                .flatMapIterable { it }
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { emitter.onComplete() }
                .subscribe {
                    runBlocking {
                        delay(200)
                        val updateContributorsJob = GlobalScope.launch {
                            cont = it
                            Log.i("Cont First updating Contributors DB starts for ", cont?.name)
                            updateDatabaseContributors(it, applicationContext, emitter)
                        }
                        updateContributorsJob.invokeOnCompletion {
                            Log.i("Cont First update Contributors DB complete ", cont?.name)
                        }
                        updateContributorsJob.join()
                    }
                }
        }
    }

    private fun updateDatabaseContributors(
        it: Contributor,
        applicationContext: Context,
        emitter: ObservableEmitter<Contributor>
    ) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        db.repositoryDao().insertContributors(it)
        Log.i("Cont First put to db: ", it.name)
        emitter.onNext(it)
    }
}
