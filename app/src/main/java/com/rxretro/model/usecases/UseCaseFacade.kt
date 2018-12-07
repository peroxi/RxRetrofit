package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.rxretro.model.dao.AppDatabase

object UseCaseFacade {
    @SuppressLint("CheckResult")
    fun fetchContributorsOfUsersRepositories(user: String, applicationContext: Context) {
        EventDataFlattener.fetchContributors(user, applicationContext)
            .distinct { contributor -> contributor.name }
            .doOnError { Log.e("Cont First", "error : " + it) }
            .doOnComplete {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "database-name"
                ).build()
                db.repositoryDao().selectContributorsOfUsersRepositories(user)
                    .flatMapIterable { it }
                    .subscribe { name: String -> Log.i("Cont First internal from db: ", name)
                    }
                //Looks like it started being called after calling onComplete() to each item of source Observable.
            }
            .subscribe { contributor -> Log.i("Cont First", contributor.name) }
    }
}
