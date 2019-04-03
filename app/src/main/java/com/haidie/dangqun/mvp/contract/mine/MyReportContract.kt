package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/13 17:26
 * description
 */
class MyReportContract {
    interface View : IBaseView{
        fun reloadMyReport()
    }

    interface Presenter : IPresenter<View>{

    }
}