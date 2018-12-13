package com.rxretro.model.dao

import android.arch.persistence.room.*
import android.util.Log
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT distinct contributor.login from contributor inner join repository on repository.login == :user")
    fun selectContributorsOfUsersRepositories(user: String?): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContributors(contributor: List<Contributor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepositories(repositories: List<Repository?>)

    @Query("delete from contributor")
    fun deleteContributors()

    @Query("delete from repository")
    fun deleteRepository()
}
