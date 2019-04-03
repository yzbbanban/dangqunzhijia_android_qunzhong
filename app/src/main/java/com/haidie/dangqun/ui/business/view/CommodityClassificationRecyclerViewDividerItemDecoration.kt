package com.haidie.dangqun.ui.business.view

import android.content.Context
import android.graphics.Color
import com.yanyusong.y_divideritemdecoration.Y_Divider
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration

/**
 * Create by   Administrator
 *      on     2018/09/05 17:30
 * description
 */
class CommodityClassificationRecyclerViewDividerItemDecoration(context: Context?)
    : Y_DividerItemDecoration(context)  {
    override fun getDivider(itemPosition: Int): Y_Divider {
        return Y_DividerBuilder()
                .setBottomSideLine(true, Color.parseColor("#f5f5f5"), 1f, 0f, 0f)
                .create()
    }
}