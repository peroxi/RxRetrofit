package com.rxretro.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rxretro.R
import com.rxretro.BR
import com.rxretro.databinding.ContributorListItemBinding
import com.rxretro.viewmodel.ContributorItemViewModel

class ContributorsAdapter: RecyclerView.Adapter<ContributorsAdapter.ContributorHolder>() {

    var layoutInflater: LayoutInflater? = null
    var data: List<String> = listOf()
    set(items) {
        field = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContributorHolder {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ContributorListItemBinding = DataBindingUtil.inflate(layoutInflater!!,
            R.layout.contributor_list_item,
            parent,
            false)
        binding.setVariable(BR.item, ContributorItemViewModel(data[position]))
        return ContributorHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ContributorHolder, position: Int) {
        holder.binding.item?.name = data[position]
    }

    class ContributorHolder(val binding: ContributorListItemBinding): RecyclerView.ViewHolder(binding.root)
}