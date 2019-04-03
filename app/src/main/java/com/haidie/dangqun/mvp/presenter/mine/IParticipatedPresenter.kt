package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.IParticipatedContract
import com.haidie.dangqun.mvp.model.bean.IParticipatedData
import com.haidie.dangqun.mvp.model.mine.IParticipatedModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/14 15:26
 * description
 */
class IParticipatedPresenter : BasePresenter<IParticipatedContract.View>(),IParticipatedContract.Presenter{
    private val iParticipatedModel by lazy { IParticipatedModel() }
    override fun getIParticipatedData(uid: Int, page: Int, size: Int, id: Int?, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = iParticipatedModel.getIParticipatedData(uid, page, size, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<IParticipatedData>("获取我参加的活动列表失败"){

                    override fun onNext(t: IParticipatedData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setIParticipatedData(t)
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