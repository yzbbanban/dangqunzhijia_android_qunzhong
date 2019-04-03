package com.haidie.dangqun.net.exception

/**
 * Create by   Administrator
 *      on     2018/09/25 13:43
 * description
 */
object ApiErrorCode {
    /**
     * 成功
     */
    const val SUCCESS = 200

    /**
     * 暂无信息
     */
    const val NO_INFO = 201
    /**
     * 网络错误
     */
    const val NETWORK_ERROR = 1000

    /**
     * 解析错误
     */
    const val PARSE_ERROR = 1001

    /**
     * 协议出错
     */
    const val HTTP_ERROR = 1002

    /**
     * 未知错误
     */
    const val UNKNOWN = 10003

    /**
     * 自定义错误
     */
    const val CUSTOM_ERROR = 1006

}