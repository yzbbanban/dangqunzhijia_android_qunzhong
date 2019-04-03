package com.haidie.dangqun.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by admin2
 *  on 2018/08/23  15:21
 * description
 */
object DateUtils {

    fun time(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault())
        val i = Integer.parseInt(time)
        return sdr.format(Date(i * 1000L))
    }

    fun getDateToString(time: String): String {
        val sdr = SimpleDateFormat("MM月dd日",Locale.getDefault())
        val i = Integer.parseInt(time)
        return sdr.format(Date(i * 1000L))
    }

    fun getTimeToChina(time: String): String {
        //        2018-07-18 16:21:53
        val strings = time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val split = strings[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val times = strings[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var month = split[1]
        var day = split[2]
        if (month.startsWith("0")) {
            month = month.substring(1)
        }
        if (day.startsWith("0")) {
            day = day.substring(1)
        }
        return month + "月" + day + "日" + times[0] + ":" + times[1]
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     */
    fun dateToStr(dateDate: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        return formatter.format(dateDate)
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd  HH:mm:ss
     */
    fun dateToString(dateDate: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
        return formatter.format(dateDate)
    }

    /**
     * 获取当前时间
     */
    fun getNowTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd HH:mm:ss
     */
    fun isDateOneBigger(str1: String, str2: String): Boolean {
        var isBigger = false
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
        var dt1: Date? = null
        var dt2: Date? = null
        try {
            dt1 = sdf.parse(str1)
            dt2 = sdf.parse(str2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (dt1!!.time >= dt2!!.time) {
            isBigger = true
        } else if (dt1.time < dt2.time) {
            isBigger = false
        }
        return isBigger
    }

}