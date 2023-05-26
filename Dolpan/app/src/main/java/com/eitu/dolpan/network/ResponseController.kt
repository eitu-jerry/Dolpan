package com.eitu.dolpan.network

import android.util.Log
import retrofit2.Call
import retrofit2.Response

abstract class ResponseController<E>(api: String, listener: OnResponseListener<E>): retrofit2.Callback<E> {

    private val api: String
    private val listener: OnResponseListener<E>

    init {
        this.api = api
        this.listener = listener
    }

    override fun onResponse(call: Call<E>, response: Response<E>) {
        if (isSuccess(response)) listener.onSuccess(response)
        else listener.onFail(response)
    }

    override fun onFailure(call: Call<E>, t: Throwable) {
        Log.d("Check", "onFailure -> " + api);
    }

    private fun isSuccess(response: Response<E>): Boolean {
        val code = response.code()
        val isSuccess = code in 200..299

        if (!isSuccess) error(response)

        return isSuccess
    }

    private fun error(response: Response<E>) {
        val error = ArrayList<String>()

        error.add("=================")
        error.add("=====에러 발생=====")
        error.add("API: " + api)
        error.add("ResponseCode: " + response.code())

        val errorBody = response.errorBody()?.string()
        if (errorBody != null) error.add(errorBody)

        error.add("=================")
        error.add("=================")

        Log.d("Check", error.joinToString("\n"))
    }

    interface OnResponseListener<E> {
        fun onSuccess(response: Response<E>)
        fun onFail(response: Response<E>)
    }

}

