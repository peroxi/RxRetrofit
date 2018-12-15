package com.rxretro.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rxretro.R
import com.rxretro.BR
import com.rxretro.databinding.ActivityMainBinding
import com.rxretro.model.usecases.ApiRequestUsecase
import com.rxretro.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this@MainActivity, R.layout.activity_main)
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainActivityViewModel::class.java)
        binding.setVariable(BR.screen, viewModel)
        viewModel?.getScreenData()?.observe(this, object : Observer<List<String>> {
            override fun onChanged(t: List<String>?) {
                viewModel?.data = t ?: listOf()
                binding.setVariable(BR.screen, viewModel)
                viewModel?.errorText?.let {
                    Toast.makeText(
                        applicationContext,
                        it, Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
