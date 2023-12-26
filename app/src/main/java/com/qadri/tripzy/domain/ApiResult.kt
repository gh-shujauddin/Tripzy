package com.qadri.tripzy.domain

sealed class ApiResult<T>(val data:T?=null, val error:String?=null){
    class Success<T>(apiResult: T):ApiResult<T>(data = apiResult)
    class Error<T>(error: String):ApiResult<T>(error = error)
    class Loading<T>:ApiResult<T>()
}