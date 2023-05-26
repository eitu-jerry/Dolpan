package com.eitu.dolpan.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<E, I: RecyclerView.ViewHolder>: RecyclerView.Adapter<I>() {

    protected val list = ArrayList<E>()

    fun setList(list: List<E>) {
        this.list.clear()
        addList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<E>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun getList(): List<E> {
        return list
    }

}