package com.haidie.dangqun.base

/**
 * Created by admin2
 *  on 2018/08/08  20:25
 * description  Presenter 基类
 */
interface IPresenter<in V : IBaseView> {
    fun attachView(mRootView : V)

    fun detachView()
}