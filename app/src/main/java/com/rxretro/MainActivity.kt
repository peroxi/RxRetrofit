package com.rxretro

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rxretro.model.usecases.UseCaseFacade
import kotlinx.android.synthetic.main.activity_main.*

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
                val text = String.format("Contributors number of %s repositories is %s", user, it.size)
                contributors_text.text = text
                progressBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    text, Toast.LENGTH_LONG
                ).show()
            }
            .flatMapIterable { it }
            .subscribe { name: String ->
                //Operations with single Contributor instances on UI thread.
                Log.i("Cont First internal from db: ", name)
            }

    }
}
