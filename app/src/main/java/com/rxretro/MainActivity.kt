package com.rxretro

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.rxretro.model.usecases.UseCaseFacade

class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = "eugenp"
        UseCaseFacade.fetchContributorsOfUsersRepositories(user, applicationContext)
            .doOnNext {
                //Operations with the LIST of Contributors on UI thread
                // (like setting/updating RecyclerView/number badge)
                Toast.makeText(
                    applicationContext,
                    String.format("Contributors number of %s repositories is %s", user, it.size), Toast.LENGTH_LONG
                ).show()
            }
            .flatMapIterable { it }
            .subscribe { name: String ->
                //Operations with single Contributor instances on UI thread.
                Log.i("Cont First internal from db: ", name)
            }

    }
}
