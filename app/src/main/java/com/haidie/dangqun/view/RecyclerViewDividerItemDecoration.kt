package com.haidie.dangqun.view

import android.content.Context
import android.graphics.Color
import com.haidie.dangqun.Constants
import com.yanyusong.y_divideritemdecoration.Y_Divider
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration


/**
 * Created by admin2
 *  on 2018/08/11  16:09
 * description
 */
class RecyclerViewDividerItemDecoration(context: Context?) : Y_DividerItemDecoration(context) {
    override fun getDivider(itemPosition: Int): Y_Divider {
        return Y_DividerBuilder()
                .setBottomSideLine(true, Color.parseColor(Constants.F_5_F_5_F_5), 1f, 0f, 0f)
                .create()
    }
}