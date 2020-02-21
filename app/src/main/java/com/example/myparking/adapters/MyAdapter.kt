package com.example.myparking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class MyAdapter<ItemClass, ItemBinding>(
    private val itemList: ArrayList<ItemClass>,
    private val itemRowLayout: Int,
    private val listener: ItemAdapterListener<ItemClass>
) :
    RecyclerView.Adapter<MyAdapter<ItemClass, ItemBinding>.MyViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    inner class MyViewHolder(val binding: ItemBinding) :
        RecyclerView.ViewHolder((binding as ViewDataBinding).root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater!!, itemRowLayout, parent, false
        )
        return MyViewHolder(binding as ItemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun updateList(list: ArrayList<ItemClass>) {
        itemList.clear()
        itemList.addAll(list)
        super.notifyDataSetChanged()
    }

    interface ItemAdapterListener<ItemClass> {
        fun onItemClicked(item: ItemClass)
    }
}

