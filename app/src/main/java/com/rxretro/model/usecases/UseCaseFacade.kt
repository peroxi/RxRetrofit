package com.rxretro.model.usecases

import android.annotation.SuppressLint
import android.util.Log

object UseCaseFacade {
    @SuppressLint("CheckResult")
    fun fetchContributorsOfUsersRepositories(user: String) {
        EventDataFlattener.fetchContributors(user)
            .distinct { contributor -> contributor.name }
            .doOnError { Log.e("Cont First", "error : " + it) }
            .subscribe { contributor -> Log.i("Cont First", contributor.name) }
    }
}
