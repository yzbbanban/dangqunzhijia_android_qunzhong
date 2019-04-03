package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.WorkOrderEvaluationContract
import com.haidie.dangqun.mvp.model.home.WorkOrderEvaluationModel
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by   Administrator
 *      on     2018/09/08 15:49
 * description
 */
class WorkOrderEvaluationPresenter : BasePresenter<WorkOrderEvaluationContract.View>(),WorkOrderEvaluationContract.Presenter{

    private val workOrderEvaluationModel by lazy { WorkOrderEvaluationModel() }

    override fun getToCommentData(uid: Int, id: Int, rank: Int, content: String, token: String, status: Int) {
        checkViewAttached()
        val disposable = workOrderEvaluationModel.getEditStatusData(uid, id, status, token)
                .flatMap{
                    if (it.data!!) {
                        workOrderEvaluationModel.getToCommentData(uid, id, rank, content, token)
                    }else{
                        Observable.empty<BaseResponse<Boolean?>>()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("操作失败"){

                    override fun onNext(t: Boolean) {
                        mRootView!!.setToCommentData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setToCommentData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}