package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyOrderDetailContract
import com.haidie.dangqun.mvp.model.bean.MyOrderDetailData
import com.haidie.dangqun.mvp.model.mine.MyOrderDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/14 14:20
 * description
 */
class MyOrderDetailPresenter : BasePresenter<MyOrderDetailContract.View>(),MyOrderDetailContract.Presenter{
    private val myOrderDetailModel by lazy { MyOrderDetailModel() }
    override fun getMyOrderDetailData(uid: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = myOrderDetailModel.getMyOrderDetailData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<List<MyOrderDetailData>>("获取详细信息失败"){
                    override fun onNext(t: List<MyOrderDetailData>) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyOrderDetailData(t[0])
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