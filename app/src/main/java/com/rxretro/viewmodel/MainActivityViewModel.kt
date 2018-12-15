package com.rxretro.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import com.rxretro.R
import com.rxretro.model.usecases.ApiRequestUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(var app: Application): AndroidViewModel(app) {
    private var screenLiveData: MutableLiveData<List<String>>? = null
    var data: List<String> = listOf()
    var isLoaded = ObservableBoolean(false)
    var loadingMessage = ObservableField<CharSequence>()

    fun getScreenData(): MutableLiveData<List<String>>? {
        if (screenLiveData == null) {
            screenLiveData = MutableLiveData()
            isLoaded.set(false)
            loadingMessage.set(app.getText(R.string.loading_message))
            loadData()
        }
        return screenLiveData
    }

    private fun loadData() {
        val user = "eugenp"
        GlobalScope.launch(Dispatchers.Main) { val responseFromDB = ApiRequestUsecase.fetchContributorsList(user, app).await()
            responseFromDB.run {
                //Operations with the LIST of Contributors on UI thread
                // (like setting/updating RecyclerView/number badge)
                val text = String.format("Contributors number of %s repositories is %s", user, data.size)
                /*contributors_text.text = text
                progressBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    text, Toast.LENGTH_LONG
                ).show()*/
                val message = if(isSuccess) null else app.getText(R.string.error_message)
                loadingMessage.set(message)
                isLoaded.set(true)
                screenLiveData?.postValue(data)
                data.iterator().forEach {
                    //Operations with single Contributor instances on UI thread.
                    Log.i("Cont First internal from db: ", it)
                }
            } }

        /*.flatMapIterable { it }
        .subscribeBy(
            onNext = {
            //Operations with single Contributor instances on UI thread.
            Log.i("Cont First internal from db: ", it)},
            onError = {
                val text = String.format("Request has failed with errorMessage %s",it.message)
                contributors_text.text = text
                progressBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    text, Toast.LENGTH_LONG
                ).show()
            }
    )*/

    }
}