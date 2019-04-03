package com.haidie.dangqun.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager


/**
 * Created by admin2
 *  on 2018/08/09  14:31
 * description  APP 相关的工具类
 */
object AppUtils {

    /**
     * 获取版本号
     */
    fun getVersionCode(context: Context): Int {
        var versionCode = -1
        try {
            val packageName = context.packageName
            versionCode = context.packageManager
                    .getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }
    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                    context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    /**
     * 获取运行最大内存
     */
    val maxMemory: Long
        get() = Runtime.getRuntime().maxMemory() / 1024

    /**
     * 获取版本信息
     */
    fun getVersionName(context: Context): String {
        var versionName = ""
        try {
            val packageName = context.packageName
            versionName = context.packageManager
                    .getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }
    /**
     * 获取设备可用内存大小
     */
    fun getDeviceUsableMemory(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        //返回当前系统的可用内存
        return (mi.availMem / (1024 * 1024)).toInt()
    }
    /**
     * 获取手机系统SDK版本
     */
    val sdkVersion: Int
        get() = android.os.Build.VERSION.SDK_INT
}