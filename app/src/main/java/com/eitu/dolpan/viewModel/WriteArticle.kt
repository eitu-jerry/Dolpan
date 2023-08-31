package com.eitu.dolpan.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.view.dialog.DialogWriteArticle


class WriteArticle : ViewModel() {

    private val isWrite = MutableLiveData<Boolean>()
    private var url : String? = null

    private lateinit var dialog : DialogWriteArticle

    fun setDialog(activity : BaseActivity) {
        dialog = DialogWriteArticle(activity, this)
        isWrite.observe(activity) {
            if (it && url != null) showDialog(url!!)
        }
    }

    private fun showDialog(url : String) {
        dialog.show(url)
    }

    fun loadUrl(url : String) {
        this.url = url
        isWrite.value = true
    }

    fun dismiss() {
        isWrite.value = false
    }

}