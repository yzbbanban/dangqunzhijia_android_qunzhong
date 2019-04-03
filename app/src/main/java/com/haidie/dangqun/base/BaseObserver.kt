package com.haidie.dangqun.base

import com.haidie.dangqun.net.exception.ApiErrorHelper
import com.haidie.dangqun.net.exception.ApiException
import io.reactivex.observers.ResourceObserver

/**
 * Create by   Administrator
 *      on     2018/09/25 13:29
 * description
 */
abstract class BaseObserver<T>(errorMsg : String) : ResourceObserver<T>() {
    private var mErrorMsg = errorMsg
    override fun onComplete() {}

    override fun onError(e: Throwable) {
        val apiException = ApiErrorHelper.handleCommonError(e, mErrorMsg)
        onFail(apiException)
    }
    abstract fun onFail(e: ApiException)
}