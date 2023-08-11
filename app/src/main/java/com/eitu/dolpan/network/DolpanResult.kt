package com.eitu.dolpan.network

import retrofit2.Response

sealed class DolpanResult<out T> {
    data class Success<out T>(val data : T) : DolpanResult<T>()
    data class Error(val exception: Exception) : DolpanResult<Nothing>()
}

fun<T> returnResult(response: Response<T>) : DolpanResult<T> {
    return if (response.isSuccessful) {
        val data = response.body()
        if (data != null) {
            DolpanResult.Success(data)
        }
        else {
            DolpanResult.Error(Exception("Call success but body is null"))
        }
    }
    else {
        DolpanResult.Error(Exception("${response.code()} : ${response.errorBody()?.string()}"))
    }
}