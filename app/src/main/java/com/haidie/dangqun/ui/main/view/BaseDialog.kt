package com.haidie.dangqun.ui.main.view

import android.app.Dialog
import android.content.Context

/**
 * Create by   Administrator
 *      on     2018/09/17 18:23
 * description
 */
class BaseDialog(context: Context?, themeResId: Int,res : Int) : Dialog(context, themeResId) {
    init {
        setContentView(res)
        setCanceledOnTouchOutside(false)
    }
}