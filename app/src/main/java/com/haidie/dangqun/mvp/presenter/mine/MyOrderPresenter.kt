package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyOrderContract
import com.haidie.dangqun.mvp.model.bean.MyOrderListData
import com.haidie.dangqun.mvp.model.mine.MyOrderModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/13 17:26
 * description
 */
class MyOrderPresenter : BasePresenter<MyOrderContract.View>(),MyOrderContract.Presenter{
    private val myOrderModel by lazy { MyOrderModel() }
    override fun getMyOrderListData(uid: Int, page: Int, size: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = myOrderModel.getMyOrderListData(uid, page, size, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MyOrderListData>("获取我的订单列表失败"){
                    override fun onNext(t: MyOrderListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyOrderListData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
}