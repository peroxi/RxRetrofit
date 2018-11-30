package com.rxretro.model.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface RepositoryDao {

    @Query("SELECT distinct contributor.login from contributor inner join repository on repository.login == :user")
    fun selectContributorsOfUsersRepositories(user: String?): Flowable<String>

    @Insert
    fun insertContributors(contributors: List<Contributor>)

    @Insert
    fun insertRepositories(repositories: List<Repository>)

    @Query("delete from contributor")
    fun deleteContributors()

    @Query("delete from repository")
    fun deleteRepository()
}
