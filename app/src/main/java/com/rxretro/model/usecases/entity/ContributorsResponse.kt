package com.rxretro.model.usecases.entity

import com.rxretro.model.entity.Contributor

class ContributorsResponse(
    val contributors: List<Contributor>,
    val errorMessage: String?
)