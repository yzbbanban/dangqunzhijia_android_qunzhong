package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/22 14:00
 * description
 */
class FaultRepairContract {
    interface View : IBaseView{
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{

    }
}