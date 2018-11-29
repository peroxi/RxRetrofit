package com.rxretro.model.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface RepositoryDao {

    @Query("SELECT contributor.login from contributor")
    fun selectContributorsOfUsersRepositories(user: String?): Flowable<String>
}
