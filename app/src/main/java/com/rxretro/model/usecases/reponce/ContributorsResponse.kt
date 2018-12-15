package com.rxretro.model.usecases.reponce

data class ContributorsResponse (val isSuccess: Boolean = true,
                                 val data: List<String>,
                                 val errorMessage: String? = null)