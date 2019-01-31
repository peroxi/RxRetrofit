package com.rxretro.model.usecases.entity

import com.rxretro.model.entity.Contributor

class ContributorsResponse(
    val contributors: List<Contributor> = listOf(),
    val contributorsNames: List<String> = listOf(),
    val errorMessage: String?
)