package com.haidie.dangqun.net.exception

/**
 * Created by admin2
 *  on 2018/08/09  13:49
 * description
 */
class ApiException(code: Int,msg: String) : RuntimeException(msg) {
    var errorCode: Int = code
    var mMessage: String = msg
}