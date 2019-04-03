package com.haidie.dangqun.mvp.contract.life

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/14 11:19
 * description
 */
class LifeListContract {
    interface View : IBaseView{
        fun reloadLife()
    }

    interface Presenter : IPresenter<View>{

    }
}