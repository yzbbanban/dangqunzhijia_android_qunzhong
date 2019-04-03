package com.haidie.dangqun.net

/**
 * Created by admin2
 *  on 2018/08/09  11:17
 * description  封装返回的数据
 */
class BaseResponse<out T> (val code : Int,
                           val message : String,
                           val data : T?)