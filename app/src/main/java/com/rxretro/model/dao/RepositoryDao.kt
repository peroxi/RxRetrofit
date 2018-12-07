package com.rxretro.model.dao

import android.arch.persistence.room.*
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface RepositoryDao {

    @Query("SELECT distinct contributor.login from contributor inner join repository on repository.login == :user")
    fun selectContributorsOfUsersRepositories(user: String?): Flowable<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContributors(contributor: Contributor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepositories(repositories: List<Repository>)

    @Query("delete from contributor")
    fun deleteContributors()

    @Query("delete from repository")
    fun deleteRepository()
}
