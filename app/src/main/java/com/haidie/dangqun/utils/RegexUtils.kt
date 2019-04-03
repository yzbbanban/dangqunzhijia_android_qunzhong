package com.haidie.dangqun.utils

import java.util.regex.Pattern

/**
 * Created by admin2
 *  on 2018/08/22  11:59
 * description
 */
object RegexUtils {
    /**
     * Return whether input matches regex of simple mobile.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileSimple(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input)
    }

    /**
     * Return whether input matches regex of exact mobile.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileExact(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input)
    }

    /**
     * Return whether input matches regex of telephone number.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isTel(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_TEL, input)
    }

    /**
     * Return whether input matches the regex.
     * @param regex The regex.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    private fun isMatch(regex: String, input: CharSequence?): Boolean {
        return input != null && input.isNotEmpty() && Pattern.matches(regex, input)
    }

    /**
     * Return whether input matches regex of id card number which length is 18.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isIDCard18(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_ID_CARD18, input)
    }
}