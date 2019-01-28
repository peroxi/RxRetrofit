package com.rxretro.model.usecases.entity

import com.rxretro.model.entity.Repository

class RepositoryResponse(
    val repositoriesList: List<Repository?>,
    val errorMessage: String?
)