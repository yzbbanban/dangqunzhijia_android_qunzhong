package com.haidie.dangqun.api

import com.haidie.dangqun.BuildConfig

/**
 * Created by admin2
 *  on 2018/08/08  20:21
 * description
 */
object UrlConstant {
    var BASE_URL_HOST = BuildConfig.API_HOST
//    private var BASE_URL_HOST_IP = "http://47.92.139.10/"
//    private var BASE_URL_HOST_ADDRESS = if (BuildConfig.DEBUG) BuildConfig.API_HOST else BASE_URL_HOST_IP
    private var BASE_URL_HOST_ADDRESS =  BuildConfig.API_HOST

    //    这是列表页
//    const val BASE_URL_LIFE = "http://www.crystal-cloud.top/index/toutiao/index"
//    var BASE_URL_LIFE = "${BASE_URL_HOST}index/toutiao/index"
    var BASE_URL_LIFE = "${BASE_URL_HOST_ADDRESS}index/toutiao/index"
    //    这是商圈列表页
//    var BASE_URL_BUSINESS = "${BASE_URL_HOST}index/goods/index"
    var BASE_URL_BUSINESS = "${BASE_URL_HOST_ADDRESS}index/goods/index"

    //    发布文章
//    const val BASE_URL_RELEASE_ARTICLE = "http://www.crystal-cloud.top/index/toutiao/add"
    var BASE_URL_RELEASE_ARTICLE = "${BASE_URL_HOST}index/toutiao/add"
    //    发布商品
    var BASE_URL_RELEASE_PRODUCT = "${BASE_URL_HOST}index/goods/add"
//    我的关注
    var BASE_URL_MY_ATTENTION = "${BASE_URL_HOST}index/toutiao/follow"
//    我的文章
    var BASE_URL_MY_ARTICLE = "${BASE_URL_HOST}index/toutiao/myarticle"
//    我的粉丝
    var BASE_URL_MY_FANS = "${BASE_URL_HOST}index/toutiao/fensi"
//    我的商品
    var BASE_URL_MY_PRODUCT = "${BASE_URL_HOST}index/goods/sale"
//    我的收藏
    var BASE_URL_MY_COLLECTION = "${BASE_URL_HOST}index/toutiao/collect"
//    我的消息
    var BASE_URL_MY_MESSAGE = "${BASE_URL_HOST}index/toutiao/comment"
//    故障报修
    var BASE_URL_FAULT_REPAIR = "${BASE_URL_HOST}index/report/index"
//    附近商家
    var BASE_URL_NEARBY_MERCHANTS_INDEX = "${BASE_URL_HOST}index/seller/index"
//    志愿者绑定页面
    var BASE_URL_VOLUNTEER_BIND = "${BASE_URL_HOST}index/volunteer/bind"
//    志愿者详情页面
    var BASE_URL_VOLUNTEER_DETAIL = "${BASE_URL_HOST}index/volunteer/detail"
//    问题上报页面
    var BASE_URL_PROBLEM_REPORT = "${BASE_URL_HOST}index/problemreport/add"
//    我的上报
    var BASE_URL_MY_REPORT = "${BASE_URL_HOST}index/problemreport/myreport"
    //    医疗健康
    var BASE_URL_MEDICAL_HEALTH = "${BASE_URL_HOST}index/medical/index"
    //    在线咨询
    var BASE_URL_ONLINE_CONSULTING = "${BASE_URL_HOST}index/olinehelp/index"
    //    家政服务
    var BASE_URL_HOUSEKEEPING = "${BASE_URL_HOST}index/housekeep/index"
    //    智慧新风-活动公告
    var BASE_URL_EVENT_ANNOUNCEMENTS = "${BASE_URL_HOST}index/newwind/index"
    //    智慧新风-活动记录
    var BASE_URL_ACTIVITY_RECORD = "${BASE_URL_HOST}index/newwinded/index"
}