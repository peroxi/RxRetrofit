package com.rxretro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rxretro.model.usecases.UseCaseFacade

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UseCaseFacade.fetchContributorsOfUsersRepositories("eugenp", applicationContext)
    }
}
