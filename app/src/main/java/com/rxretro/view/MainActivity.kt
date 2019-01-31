package com.rxretro.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.rxretro.BR
import com.rxretro.R
import com.rxretro.databinding.ActivityMainBinding
import com.rxretro.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private var viewModel: MainActivityViewModel? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this@MainActivity, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.setVariable(BR.screen, viewModel)

        val user = "eugenp"
        viewModel?.getActivityData(savedInstanceState == null, user)?.observe(this, object : Observer<List<String>> {
            override fun onChanged(t: List<String>?) {
                binding.setVariable(BR.screen, viewModel)
                viewModel?.run {
                    Toast.makeText(
                        applicationContext,
                        loadingMessage.get(), Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
