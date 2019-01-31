package com.rxretro.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.rxretro.R
import com.rxretro.model.usecases.ContributorsOfUsersRepositoriesUseCase
import com.rxretro.model.usecases.entity.ContributorsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(var app: Application) : AndroidViewModel(app) {
    private var mainActivityLiveData: MutableLiveData<List<String>>? = null
    var loadedData: List<String> = listOf()
    var isLoaded = ObservableBoolean(false)
    var loadingMessage = ObservableField<CharSequence>()
    var errorText: String? = null
    var userName = ObservableField<CharSequence>()

    fun getActivityData(reload: Boolean, name: String): MutableLiveData<List<String>>? {
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
                    contributorsNames = ContributorsOfUsersRepositoriesUseCase.fetchContributorsOfUsersRepositoriesAsync(
                        "eugenp",
                        app
                    ).await(),
                    errorMessage = null
                )
            } else {
                ContributorsResponse(contributorsNames = loadedData, errorMessage = null)
            }
            contributorsResponse.run {
                val message = if (errorMessage == null) {
                    String.format(
                        app.getText(R.string.repositories_message).toString(),
                        userName.get(),
                        contributorsNames.size
                    )
                } else {
                    app.getText(R.string.error_message)
                }
                loadingMessage.set(message)
                isLoaded.set(true)
                mainActivityLiveData?.postValue(contributorsNames)
                errorText = errorMessage
                loadedData = contributorsNames
            }
        }
    }
}