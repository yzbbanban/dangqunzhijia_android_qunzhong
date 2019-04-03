package com.haidie.dangqun.utils

/**
 * Created by admin2
 *  on 2018/08/22  11:57
 * description
 */
object RegexConstants {

    /**
     * Regex of simple mobile.
     */
    const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"

    /**
     * Regex of exact mobile.
     * china mobile: 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198
     * china unicom: 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186
     * china telecom: 133, 153, 173, 177, 180, 181, 189, 199
     * global star: 1349
     * virtual operator: 170
     */
    const val REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$"

    /**
     * Regex of telephone number.
     */
    const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"

    /**
     * Regex of id card number which length is 18.
     */
    const val REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

}