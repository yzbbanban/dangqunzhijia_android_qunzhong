package com.haidie.dangqun.mvp.contract.life

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Created by admin2
 *  on 2018/08/13  19:56
 * description
 */
interface LifeContract {
    interface View : IBaseView {
        fun reloadLife()
    }

    interface Presenter : IPresenter<View> {

    }
}