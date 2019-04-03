package com.haidie.dangqun.utils

import android.annotation.SuppressLint
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by admin2
 *  on 2018/08/13  16:51
 * description
 */
object LogHelper {

    private const val TAG = "LogHelper"

    fun v(msg: String) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = "[" + traceElement.fileName + " | " +
                traceElement.lineNumber + " | " + traceElement.methodName + "]" + msg
        Logger.t(TAG).i(toStringBuffer)
    }

    fun d(msg: String) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = "[" + traceElement.fileName + " | " +
                traceElement.lineNumber + " | " + traceElement.methodName + "] " + msg
        Logger.t(TAG).i(toStringBuffer)
    }

    fun i(msg: String) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = "[" + traceElement.fileName + " | " +
                traceElement.lineNumber + " | " + traceElement.methodName + "] " + msg
        Logger.t(TAG).i(toStringBuffer)
    }

    fun w(msg: String) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = "[" + traceElement.fileName + " | " +
                traceElement.lineNumber + " | " + traceElement.methodName + "] " + msg
        Logger.t(TAG).w(toStringBuffer)
    }

    fun e(msg: String) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = "[" + traceElement.fileName + " | " +
                traceElement.lineNumber + " | " + traceElement.methodName + "] " + msg
        Logger.t(TAG).e(toStringBuffer)
    }

    fun v(messages: Array<String>) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = StringBuilder("[").append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ").append(traceElement.methodName).append("] ")
        toStringBuffer.append("Log.v")
        for (msg in messages) {
            toStringBuffer.append(String.format("===%s", msg))
        }
        Logger.t(TAG).i(toStringBuffer.toString())
    }

    fun d(messages: Array<String>) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = StringBuilder("[").append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ").append(traceElement.methodName).append("] ")
        toStringBuffer.append("Log.d")
        for (msg in messages) {
            toStringBuffer.append(String.format("===%s", msg))
        }
        Logger.t(TAG).i(toStringBuffer.toString())
    }

    fun i(messages: Array<String>) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = StringBuilder("[").append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ").append(traceElement.methodName).append("] ")
        toStringBuffer.append("Log.i")
        for (msg in messages) {
            toStringBuffer.append(String.format("===%s", msg))
        }
        Logger.t(TAG).i(toStringBuffer.toString())
    }

    fun w(messages: Array<String>) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = StringBuilder("[").append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ").append(traceElement.methodName).append("] ")
        toStringBuffer.append("Log.w")
        for (msg in messages) {
            toStringBuffer.append(String.format("===%s", msg))
        }
        Logger.t(TAG).w(toStringBuffer.toString())
    }

    fun e(messages: Array<String>) {
        val traceElement = Exception().stackTrace[1]
        val toStringBuffer = StringBuilder("[").append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ").append(traceElement.methodName).append("] ")
        toStringBuffer.append("Log.e")
        for (msg in messages) {
            toStringBuffer.append(String.format("===%s", msg))
        }
        Logger.t(TAG).e(toStringBuffer.toString())
    }

    // 当前文件名
    fun file(): String {
        val traceElement = Exception().stackTrace[1]
        return traceElement.fileName
    }

    // 当前方法名
    fun func(): String {
        val traceElement = Exception().stackTrace[1]
        return traceElement.methodName
    }

    // 当前行号
    fun line(): Int {
        val traceElement = Exception().stackTrace[1]
        return traceElement.lineNumber
    }

    // 当前时间
    @SuppressLint("SimpleDateFormat")
    fun time(): String {
        val now = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return sdf.format(now)
    }
}