package com.haidie.dangqun.rx

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Create by   Administrator
 *      on     2018/09/25 14:55
 * description
 */
object RxUtils {
    /**
     * 统一返回结果处理
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
    </T> */
    fun <T> handleResult(): ObservableTransformer<BaseResponse<T>, T> {
       return ObservableTransformer{ it ->
           it.flatMap{
               if (it.code == ApiErrorCode.SUCCESS && it.data != null ) {
                   createData(it.data)
               } else {
                   Observable.error(ApiException(it.code,it.message))
               }
           }
       }
    }

    /**
     * 得到 Observable
     * @param <T> 指定的泛型类型
     * @return Observable
    </T> */
    private fun <T> createData(t: T): Observable<T> {
        return Observable.create {
            try {
                it.onNext(t)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }
}