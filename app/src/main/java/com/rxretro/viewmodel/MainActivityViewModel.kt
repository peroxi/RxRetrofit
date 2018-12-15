package com.rxretro.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.rxretro.model.usecases.ApiRequestUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(var app: Application): AndroidViewModel(app) {
    private var screenLiveData: MutableLiveData<List<String>>? = null
    var data: List<String> = listOf()

    fun getScreenData(): MutableLiveData<List<String>>? {
        if (screenLiveData == null) {
            screenLiveData = MutableLiveData<List<String>>()
            loadData()
        }
        return screenLiveData
    }

    private fun loadData() {
        val user = "eugenp"
        GlobalScope.launch(Dispatchers.Main) { val listFromDB = ApiRequestUsecase.fetchContributorsList(user, app).await()
            listFromDB.run {
                //Operations with the LIST of Contributors on UI thread
                // (like setting/updating RecyclerView/number badge)
                val text = String.format("Contributors number of %s repositories is %s", user, this.size)
                /*contributors_text.text = text
                progressBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    text, Toast.LENGTH_LONG
                ).show()*/
                screenLiveData?.postValue(this)
                iterator().forEach {
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