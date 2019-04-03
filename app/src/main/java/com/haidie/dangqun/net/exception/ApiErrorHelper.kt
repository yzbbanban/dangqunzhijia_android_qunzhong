package com.haidie.dangqun.net.exception

import com.google.gson.JsonParseException
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Create by   Administrator
 *      on     2018/09/25 13:35
 * description
 */
object ApiErrorHelper {
    fun handleCommonError(e: Throwable, mErrorMsg: String): ApiException {
        val ex: ApiException
        if (e is HttpException) {             //HTTP错误
            ex = ApiException(ApiErrorCode.HTTP_ERROR, e.message())
            ex.mMessage = MyApplication.context.getString(R.string.http_error)  //均视为网络错误
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            ex = ApiException(ApiErrorCode.PARSE_ERROR, e.message!!)
            ex.mMessage = MyApplication.context.getString(R.string.failed_to_parse)            //均视为解析错误
        } else if (e is SocketTimeoutException ||
                e is ConnectException ||
                e is UnknownHostException) {
            ex = ApiException(ApiErrorCode.NETWORK_ERROR, e.message!!)
            ex.mMessage = MyApplication.context.getString(R.string.failed_to_connect)  //均视为网络错误
        } else if (e is ApiException) {    //服务器返回的错误
            ex = e
        } else if (mErrorMsg.isNotEmpty()) {
            ex = ApiException(ApiErrorCode.CUSTOM_ERROR, e.message!!)
            ex.mMessage = mErrorMsg
        } else {
            ex = ApiException(ApiErrorCode.UNKNOWN, e.message!!)
            ex.mMessage = MyApplication.context.getString(R.string.unKnown_error)          //未知错误
        }
        return ex
    }
}