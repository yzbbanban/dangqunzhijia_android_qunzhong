package com.haidie.dangqun.utils

import android.content.Context
import com.haidie.dangqun.Constants
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.*
import java.nio.channels.FileChannel


/**
 * Create by Administrator
 * on 2018/08/27 16:25
 */
object CommonUtils {

    /**
     * 根据文件路径拷贝文件
     */
    fun copyFile(src: File?, destPath: String?, FileName: String): Boolean {
        var result = false
        if (src == null || destPath == null) {
            return result
        }
        val dest = File(destPath + FileName)
        if (dest.exists()) {
            dest.delete() // delete file
        }
        try {
            dest.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val srcChannel: FileChannel?
        val dstChannel: FileChannel?
        try {
            srcChannel = FileInputStream(src).channel
            dstChannel = FileOutputStream(dest).channel
            srcChannel.transferTo(0, srcChannel.size(), dstChannel)
            result = true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            return result
        }

        try {
            srcChannel.close()
            dstChannel.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 判断微信客户端是否存在
     * @return true安装, false未安装
     */
    fun isWeChatAppInstalled(context: Context): Boolean {
        val api = WXAPIFactory.createWXAPI(context, Constants.APP_ID)
        if (api.isWXAppInstalled) {
            return true
        } else {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getInstalledPackages(0)
            if (packageInfo != null) {
                for (i in packageInfo.indices) {
                    val pn = packageInfo[i].packageName
                    if (pn.equals("com.tencent.mm", ignoreCase = true)) {
                        return true
                    }
                }
            }
            return false
        }
    }

    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open(fileName)))
            var line: String? = ""
            while (true){
                stringBuilder.append(line)
                line = bf.readLine()?: break
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private var lastClickTime: Long = 0
    private const val SPACE_TIME = 500
    fun isDoubleClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        val isClick2: Boolean
        isClick2 = currentTime - lastClickTime <= SPACE_TIME
        lastClickTime = currentTime
        return isClick2
    }
}