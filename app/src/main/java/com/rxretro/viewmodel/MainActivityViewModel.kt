package com.rxretro.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.rxretro.R
import com.rxretro.model.entity.Contributor
import com.rxretro.model.usecases.ContributorsOfUsersRepositoriesUseCase
import com.rxretro.model.usecases.entity.ContributorsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(var app: Application) : AndroidViewModel(app) {
    private var mainActivityLiveData: MutableLiveData<List<Contributor>>? = null
    var loadedData: List<Contributor> = listOf()
    var isLoaded = ObservableBoolean(false)
    var loadingMessage = ObservableField<CharSequence>()
    var errorText: String? = null
    var userName = ObservableField<CharSequence>()

    fun getActivityData(reload: Boolean, name: String): MutableLiveData<List<Contributor>>? {
        userName.set(name)
        if (mainActivityLiveData == null) {
            mainActivityLiveData = MutableLiveData()
        }
        isLoaded.set(false)
        loadingMessage.set(app.getText(R.string.loading_message))
        loadData(reload)
        return mainActivityLiveData
    }

    private fun loadData(reload: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            val contributorsResponse: ContributorsResponse = if (reload) {
                ContributorsResponse(
                    contributors = ContributorsOfUsersRepositoriesUseCase.fetchContributorsOfUsersRepositoriesAsync(
                        "eugenp",
                        app
                    ).await(),
                    errorMessage = null
                )
            } else {
                ContributorsResponse(contributors = loadedData, errorMessage = null)
            }
            contributorsResponse.run {
                val message = if (errorMessage == null) {
                    String.format(
                        app.getText(R.string.repositories_message).toString(),
                        userName.get(),
                        contributors.size
                    )
                } else {
                    app.getText(R.string.error_message)
                }
                loadingMessage.set(message)
                isLoaded.set(true)
                mainActivityLiveData?.postValue(contributors)
                errorText = errorMessage
                loadedData = contributors
            }
        }
    }
}