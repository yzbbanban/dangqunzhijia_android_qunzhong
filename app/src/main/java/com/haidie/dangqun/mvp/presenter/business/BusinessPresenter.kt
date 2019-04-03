package com.haidie.dangqun.mvp.presenter.business

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.business.BusinessContract
import com.haidie.dangqun.mvp.event.ReloadBusinessEvent
import com.haidie.dangqun.mvp.model.business.BusinessModel
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Created by admin2
 *  on 2018/08/13  19:57
 * description
 */
class BusinessPresenter : BasePresenter<BusinessContract.View>(), BusinessContract.Presenter {
    private val businessModel by lazy { BusinessModel() }
    override fun attachView(mRootView: BusinessContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadBusinessEvent::class.java)
                .subscribe { mRootView!!.reloadBusiness() })
    }

    override fun getCommodityClassificationListData(uid: Int, token: String) {
        checkViewAttached()
        val disposable = businessModel.getCommodityClassificationListData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<CommodityClassificationListData>>("商品分类列表获取失败"){
                    override fun onNext(t: ArrayList<CommodityClassificationListData>) {  mRootView!!.setCommodityClassificationListData(t) }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage,e.errorCode) }
                })

        addSubscription(disposable)
    }
}