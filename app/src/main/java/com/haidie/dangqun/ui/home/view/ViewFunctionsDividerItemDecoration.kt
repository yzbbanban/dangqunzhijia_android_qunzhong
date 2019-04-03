package com.haidie.dangqun.ui.home.view

import android.content.Context
import android.graphics.Color
import com.haidie.dangqun.Constants
import com.yanyusong.y_divideritemdecoration.Y_Divider
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration

/**
 * Create by   Administrator
 *      on     2018/10/18 13:33
 * description
 */
class ViewFunctionsDividerItemDecoration(context: Context?) : Y_DividerItemDecoration(context) {
    override fun getDivider(itemPosition: Int): Y_Divider {
        var divider: Y_Divider? = null
        when (itemPosition % 4) {
            0,1,2 ->
                //每一行第一个、第二个和第三个显示right和bottom
                divider = Y_DividerBuilder()
                        .setRightSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 1f, 0f, 0f)
                        .setBottomSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 1f, 0f, 0f)
                        .create()
            3 ->
                //最后一个只显示bottom
                divider = Y_DividerBuilder()
                        .setBottomSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 1f, 0f, 0f)
                        .create()
        }
        return divider!!
    }
}