package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyVoteContract
import com.haidie.dangqun.mvp.event.ReleaseVoteEvent
import com.haidie.dangqun.mvp.model.bean.MyVoteListData
import com.haidie.dangqun.mvp.model.mine.MyVoteModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/17 13:33
 * description
 */
class MyVotePresenter : BasePresenter<MyVoteContract.View>(),MyVoteContract.Presenter{
    private val myVoteModel by lazy { MyVoteModel() }
    override fun attachView(mRootView: MyVoteContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReleaseVoteEvent::class.java)
                .subscribe { mRootView!!.showRefreshEvent() })
    }
    override fun getMyVoteListData(uid: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = myVoteModel.getMyVoteListData(uid, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MyVoteListData>("获取投票列表数据失败"){
                    override fun onNext(t: MyVoteListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyVoteListData(t)
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