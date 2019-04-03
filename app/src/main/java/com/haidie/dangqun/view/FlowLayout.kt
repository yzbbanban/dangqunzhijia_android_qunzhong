package com.haidie.dangqun.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.haidie.dangqun.utils.LogHelper

/**
 * Create by   Administrator
 *      on     2018/12/06 10:40
 * description
 */
class FlowLayout : ViewGroup {
    constructor(context : Context): super(context)
    constructor(context : Context,attrs : AttributeSet): super(context,attrs)
    constructor(context : Context,attrs : AttributeSet,defStyleAttr : Int): super(context,attrs,defStyleAttr)

    /**
     * 储存所有的view 按行记录
     */
    private val mAllViews = ArrayList<List<View>>()
    /**
     * 记录每一行的高度
     */
    private val mLineHeight = ArrayList<Int>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 置空 view 容器 和 lineHeight 容器  重新赋值
        //因为OnMeasure方法会走两次，第一次是实例化这个对象的时候高度和宽度都是0
        //之后走了OnSizeChange()方法后 又走了一次OnMeasure，所以要把第一次加进去的数据清空。
        mAllViews.clear()
        mLineHeight.clear()
        //得到上级容器为其推荐的宽高和计算模式
        val specWidthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specHeightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val specWidthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val specHeightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        // 计算出所有的 child 的 宽和高
//      measureChildren(specWidthSize, specHeightSize);
        // 记录如果是 warp_content 是设置的宽和高
        var width = 0
        var height = 0
        // 得到子view的个数
        val cCount = childCount
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        var lineWidth = 0
        /**
         * 每一行的高度，累加至height
         */
        var lineHeight = 0

        // 存储每一行所有的childView
        var lineViews: MutableList<View> = ArrayList()

        for (i in 0 until cCount) {
            // 得到每个子View
            val child = getChildAt(i)
            // 测量每个子View的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            // 当前子view的lp
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams
            // 子view的宽和高
            var cWidth: Int
            var cheight: Int
            // 当前子 view 实际占的宽
            cWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            // 当前子View 实际占的高
            cheight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            lineHeight = cheight
            // 需要换行
            if (lineWidth + cWidth > specWidthSize) {
                width = Math.max(lineWidth, cWidth)// 取最大值
                lineWidth = cWidth // 开启新行的时候重新累加width
                // 开启新行时累加 height
                //              lineHeight = height; // 记录下一行的高度
                mAllViews.add(lineViews)
                mLineHeight.add(cheight)
                lineViews = ArrayList()
                // 换行的时候把该 view 放进 集合里
                lineViews.add(child)// 这个  view(child) 是下一行的第一个view
                height += cheight //每个View高度是一样的，直接累加
                LogHelper.d("需要换行 ========== height--$height")
                LogHelper.d("onMeasure ========= AllViews.size()  --  > " + mAllViews.size)
            } else {
                // 不需要换行
                lineWidth += cWidth//
                LogHelper.d("不需要换行 ======== height--$height")
                // 不需要换行时 把子View add 进集合
                lineViews.add(child)
            }

            if (i == cCount - 1) {
                // 如果是最后一个view
                width = Math.max(lineWidth, cWidth)
                height += cheight
                LogHelper.d("最后一个view ========= height--$height")
            }
        }
        // 循环结束后 把最后一行内容add进集合中
        mLineHeight.add(lineHeight) // 记录最后一行
        mAllViews.add(lineViews)
        // MeasureSpec.EXACTLY 表示设置了精确的值
        // 如果 mode 是 MeasureSpec.EXACTLY 时候，则不是 warp_content 用计算来的值，否则则用上级布局分给它的值
        setMeasuredDimension(
                if (specWidthMode == View.MeasureSpec.EXACTLY) specWidthSize else width,
                if (specHeightMode == View.MeasureSpec.EXACTLY) specHeightSize else height
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 当前行的最大高度
        var lineHeight: Int
        // 存储每一行所有的childView
        var lineViews: List<View>
        var left = 0
        var top = 0
        // 得到总行数
        val lineNums = mAllViews.size
        for (i in 0 until lineNums) {
            // 每一行的所有的views
            lineViews = mAllViews[i]
            // 当前行的最大高度
            lineHeight = mLineHeight[i]

            LogHelper.d("onLayout ======= 第" + i + "行 ：" + lineViews.size + "-------lineHeight" + lineHeight)

            // 遍历当前行所有的View
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child.visibility == View.GONE) {
                    continue
                }
                val lp = child.layoutParams as ViewGroup.MarginLayoutParams

                //计算childView的left,top,right,bottom
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val rc = lc + child.measuredWidth
                val bc = tc + child.measuredHeight

                child.layout(lc, tc, rc, bc)

                left += child.measuredWidth + lp.rightMargin + lp.leftMargin
            }
            left = 0
            top += lineHeight
        }
    }

    /**
     * 这个一定要设置，否则会包强转错误
     * 设置它支持 marginLayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {

        return ViewGroup.MarginLayoutParams(context, attrs)
    }
}