package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.HomeBannerData
import com.haidie.dangqun.mvp.model.bean.HomeNewsData


/**
 * Created by admin2
 *  on 2018/08/10  10:15
 * description  契约类
 */
class HomeContract {
    interface View : IBaseView {
        fun setHomeBannerData( homeBannerData: HomeBannerData)

        fun setHomeNewsData(homeNewsData: HomeNewsData)
        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getHomeData( user_id: Int,  token: String)

    }
}