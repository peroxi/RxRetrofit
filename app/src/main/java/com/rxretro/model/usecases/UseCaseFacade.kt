package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

object UseCaseFacade {

    @SuppressLint("CheckResult")
    fun fetchContributorsOfUsersRepositories(user: String, applicationContext: Context): Flowable<List<String>> {
        val dbSubject: PublishSubject<List<String>> = PublishSubject.create()
        EventDataFlattener.fetchContributors(user, applicationContext)
            .distinct { contributor -> contributor.name }
            .doOnError { Log.e("Cont First", "error : " + it) }
            .doOnComplete {
                fetchFromDB(applicationContext, user).toObservable()
                    .subscribe { dbSubject.onNext(it) }
                //Looks like it started being called after calling onComplete() to each item of source Observable.
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { contributor -> Log.i("Cont First", contributor.name) }
        return dbSubject.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    private fun fetchFromDB(applicationContext: Context, user: String): Flowable<List<String>> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        return db.repositoryDao().selectContributorsOfUsersRepositories(user)
            .observeOn(AndroidSchedulers.mainThread())
    }
}
