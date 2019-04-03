package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.AllFunctionsDataBean

/**
 * Created by admin2
 *  on 2018/08/21  17:09
 * description
 */
class AllFunctionsContract {
    interface View : IBaseView {
        fun setFunctionsData(functionsDataList : ArrayList<AllFunctionsDataBean> )
    }

    interface Presenter : IPresenter<View> {
        fun initFunctionsData()
    }
}