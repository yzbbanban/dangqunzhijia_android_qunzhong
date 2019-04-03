package com.haidie.dangqun.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by admin2
 *  on 2018/08/09  10:56
 * description
 */
object DisplayManager {

    private var displayMetrics: DisplayMetrics? = null
    private var screenWidth: Int? = null
    private var screenHeight: Int? = null
    private var screenDpi: Int? = null
    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }

    private const val STANDARD_WIDTH = 1080
    private const val STANDARD_HEIGHT = 1920
    fun getScreenWidth(): Int? {
        return screenWidth
    }

    private fun getScreenHeight(): Int? {
        return screenHeight
    }

    /**
     * 传入UI图中问题的高度，单位像素
     */
    fun getPaintSize(size: Int): Int? {
        return getRealHeight(size)
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     * @param px ui图中的大小
     */
    fun getRealWidth(px: Int): Int? {
        //ui图的宽度
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     */
    private fun getRealWidth(px: Int, parentWidth: Float): Int? {
        return (px / parentWidth * getScreenWidth()!!).toInt()
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     * @param px ui图中的大小
     */
    private fun getRealHeight(px: Int): Int? {
        //ui图的宽度
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     * @param px           ui图中的大小
     * @param parentHeight 父view在ui图中的高度
     */
    private fun getRealHeight(px: Int, parentHeight: Float): Int? {
        return (px / parentHeight * getScreenHeight()!!).toInt()
    }

    /**
     * dip转px
     */
    fun dip2px(dipValue: Float): Int? {
        val scale = displayMetrics?.density
        return (dipValue * scale!! + 0.5f).toInt()
    }
}