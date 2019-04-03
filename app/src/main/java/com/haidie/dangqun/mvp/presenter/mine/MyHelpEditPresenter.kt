package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyHelpEditContract
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.model.mine.MyHelpEditModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/10/23 14:23
 * description
 */
class MyHelpEditPresenter : BasePresenter<MyHelpEditContract.View>(), MyHelpEditContract.Presenter {

    private val myHelpEditModel by lazy { MyHelpEditModel() }
    override fun getOnlineHelpDetailsData(uid: Int, id: Int, token: String, page: Int, size: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = myHelpEditModel.getOnlineHelpDetailsData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<OnlineHelpDetailsData>("获取网上求助详情失败") {
                    override fun onNext(t: OnlineHelpDetailsData) {
                        mRootView?.apply {
                            dismissLoading()
                            setOnlineHelpDetailsData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage, e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
    override fun getMyHelpEditData(uid: RequestBody, token: RequestBody, id: RequestBody, type: RequestBody?,
                                   troubletype: RequestBody, is_online: RequestBody, username: RequestBody,
                                   gender: RequestBody, phone: RequestBody, identity: RequestBody, address: RequestBody?,
                                   title: RequestBody, content: RequestBody,  pic1: RequestBody?,  pic2: RequestBody?,
                                   pic3: RequestBody?,  part1: MultipartBody.Part?,  part2: MultipartBody.Part?,
                                   part3: MultipartBody.Part?) {
        val disposable = myHelpEditModel.getMyHelpEditData(uid, token, id, type, troubletype, is_online, username, gender, phone, identity, address, title, content,
                pic1, pic2, pic3,part1,part2,part3)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("编辑失败") {
                    override fun onNext(t: Boolean) {
                        mRootView?.setMyHelpEditData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setMyHelpEditData(false, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}