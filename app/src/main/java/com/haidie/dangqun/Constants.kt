package com.haidie.dangqun

import android.os.Environment
import java.io.File

/**
 * Created by admin2
 *  on 2018/08/10  15:06
 * description  常量
 */
object Constants {
    //    微信注册 AppID：wx3b3e9fc8cd1b6d59
    const val APP_ID = "wx3b3e9fc8cd1b6d59"

    const val ICON = "icon"
    const val TEXT = "text"

    const val CURRENT_TAB_INDEX = "currentTabIndex"
    //    字体相关
    const val FONTS_REGULAR = "fonts/SourceHanSansCN-Regular.ttf"
    const val FONTS_NORMAL = "fonts/SourceHanSansCN-Normal.ttf"
    const val FONTS_MEDIUM = "fonts/SourceHanSansCN-Medium.ttf"
    //    账号相关
    const val TOKEN = "token"
    const val UID = "uid"
    const val ACCOUNT = "account"
    const val PASSWORD = "password"
    const val DEVICE_ID = "device_id"
    const val LOGIN_STATUS = "login_status"
    const val USERNAME = "username"
    const val RESET_PWD = "resetpwd"
    const val REGISTER = "register"
    const val MOBILE = "mobile"
    //    个人信息
    const val AVATAR = "avatar"
    const val NICKNAME = "nickname"
    const val GENDER = "gender"
    const val BIRTHDAY = "birthday"
    const val PHONE = "phone"

    const val PAGE = 1
    const val SIZE = 10
    const val ID = "id"
//    类型
    const val TYPE = "type"

    const val ARG_PARAM1 = "param1"
    const val ARG_PARAM2 = "param2"
//    文章
    const val ARTICLE = "article"
//    活动
    const val ACTIVITY = "activity"
//    投票
    const val VOTE = "vote"
//    志愿者
    const val VOLUNTEER = "volunteer"
//    待审核志愿者
    const val PENDING_VOLUNTEER = "pending_volunteer"
    const val GROUP_ID = "group_id"
    const val TAB = "tab"
    const val IS_OFFICIAL = "is_official"
    const val IS_REPORT_OR_HELP = "is_report_or_help"
    const val IS_HELP = "is_help"
    const val IS_SIGN_UP = "is_sign_up"
    const val IS_SIGN_IN_OR_OUT = "is_sign_in_or_out"
    //    JS方法
    const val SAVE_DATA = "saveData"
    //    分类
    const val CATEGORY = "category"
    //    刷新数据
    const val RELOAD = "Reload"

    const val ANDROID = "android"
    const val URL_KEY = "url"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val ADDRESS_STR = "address_Str"
    const val CHINESE = "chinese"
    const val MATH = "math"
    const val ENGLISH = "english"

//    WebView相关
    const val UTF_8 = "utf-8"
    const val TEXT_HTML = "text/html"
    const val HTML_BODY = "<html><body>"
    const val BODY_HTML = "</body></html>"
    const val IMAGE = "image/*"

    const val OK_HTTP = "OkHttp"
    //    空字符串
    const val EMPTY_STRING = ""
    //    空字符零
    const val STRING_ZERO = "0"
    //    默认-1
    const val NEGATIVE_ONE = -1
    const val DEFAULT_FALSE = false
    //    微信支付
    const val PAYMENT_METHOD_WEI_CHAT = 1
    //    支付宝支付
    const val PAYMENT_METHOD_ALI_PAY = 2
    //    我的
    const val MY_TYPE_COLLECTION  = 1
    const val MY_TYPE_FANS        = 2
    const val MY_TYPE_ARTICLE     = 3
    const val MY_TYPE_PRODUCT     = 4
    const val MY_TYPE_MESSAGE     = 5
    const val MY_TYPE_ATTENTION   = 6

    const val EVENT_ANNOUNCEMENTS  = 1
    const val ACTIVITY_RECORD      = 2

    const val PIC = "pic"
    const val PNG = ".png"
    const val PIC_PNG = "pic.png"
    const val MULTIPART_FORM_DATA = "multipart/form-data"
    const val F_5_F_5_F_5 = "#f5f5f5"
    //    图片路径
    var PATH_PIC = MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath + File.separatorChar

    //    下载路径
    var PATH_APK = MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separatorChar
}