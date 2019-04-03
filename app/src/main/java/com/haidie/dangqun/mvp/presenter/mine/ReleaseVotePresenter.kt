package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.ReleaseVoteContract
import com.haidie.dangqun.mvp.model.mine.ReleaseVoteModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/17 14:20
 * description
 */
class ReleaseVotePresenter : BasePresenter<ReleaseVoteContract.View>(),ReleaseVoteContract.Presenter{
    private val releaseVoteModel by lazy { ReleaseVoteModel() }
    override fun getReleaseVoteData(uid: RequestBody, token: RequestBody, type: RequestBody, title: RequestBody, content: RequestBody,
                                    start_time: RequestBody, end_time: RequestBody, choose: RequestBody, part: MultipartBody.Part) {
        checkViewAttached()
        val disposable = releaseVoteModel.getReleaseVoteData(uid, token, type, title, content, start_time, end_time, choose, part)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("发布失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setReleaseVoteData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            if (e.errorCode == ApiErrorCode.SUCCESS){
                                setReleaseVoteData(false,e.mMessage)
                            }else{
                                showError(e.mMessage,e.errorCode)
                            }
                        }
                    }
                })
        addSubscription(disposable)
    }
}