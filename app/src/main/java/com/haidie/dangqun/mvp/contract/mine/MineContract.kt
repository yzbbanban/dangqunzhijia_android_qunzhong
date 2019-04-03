package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MineData

/**
 * Created by admin2
 *  on 2018/08/10  14:51
 * description
 */
interface MineContract {
    interface View : IBaseView {
        fun setMineData(mineData: MineData)

        fun showError(msg : String,errorCode : Int)
        fun logoutSuccess()
        fun showRefreshEvent()
    }
    interface Presenter : IPresenter<View> {
        fun getMineData(uid: Int,token: String)

        fun getLogoutData(uid: Int,token: String)
    }
}