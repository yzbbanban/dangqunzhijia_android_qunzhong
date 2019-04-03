package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.CommodityTradingContract
import com.haidie.dangqun.mvp.event.ReloadBusinessEvent
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.mvp.model.home.CommodityTradingModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/10/12 13:26
 * description
 */
class CommodityTradingPresenter : BasePresenter<CommodityTradingContract.View>(),CommodityTradingContract.Presenter{
    private val commodityTradingModel by lazy { CommodityTradingModel() }
    override fun attachView(mRootView: CommodityTradingContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadBusinessEvent::class.java)
                .subscribe { mRootView!!.reloadBusiness() })
    }
    override fun getCommodityClassificationListData(uid: Int, token: String) {
        checkViewAttached()
        val disposable = commodityTradingModel.getCommodityClassificationListData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<CommodityClassificationListData>>("商品分类列表获取失败"){
                    override fun onNext(t: ArrayList<CommodityClassificationListData>) {  mRootView!!.setCommodityClassificationListData(t) }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage,e.errorCode) }
                })
        addSubscription(disposable)
    }
}