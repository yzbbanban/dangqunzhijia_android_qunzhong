package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.IParticipatedData

/**
 * Create by   Administrator
 *      on     2018/09/14 15:25
 * description
 */
class IParticipatedContract {
    interface View : IBaseView{
        fun setIParticipatedData(iParticipatedData: IParticipatedData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getIParticipatedData( uid: Int,  page: Int,  size: Int, id: Int?,  token: String)
    }
}