package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.OnlineHelpFormSubmissionContract
import com.haidie.dangqun.mvp.model.bean.BlockInfoData
import com.haidie.dangqun.mvp.model.bean.HouseListData
import com.haidie.dangqun.mvp.model.bean.UnitListData
import com.haidie.dangqun.mvp.model.home.OnlineHelpFormSubmissionModel
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.utils.LogHelper
import io.reactivex.Observable
import io.reactivex.functions.Function
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/12 16:36
 * description
 */
class OnlineHelpFormSubmissionPresenter : BasePresenter<OnlineHelpFormSubmissionContract.View>(), OnlineHelpFormSubmissionContract.Presenter {

    private val onlineHelpFormSubmissionModel by lazy { OnlineHelpFormSubmissionModel() }

    override fun getBlockData(uid: Int, token: String) {
        checkViewAttached()
        val disposable = onlineHelpFormSubmissionModel.getBlockInfoData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<BlockInfoData>>("获取楼栋列表失败") {
                    override fun onNext(t: ArrayList<BlockInfoData>) {
                        mRootView?.setBlockListData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showError(e.mMessage, e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
    override fun getUnitListData(uid: Int, token: String, title: String) {
        val disposable = onlineHelpFormSubmissionModel.getUnitListData(uid, token, title)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<UnitListData>>("获取单元的列表失败") {
                    override fun onNext(t: ArrayList<UnitListData>) {
                        mRootView?.setUnitListData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showError(e.mMessage, e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
    override fun getHouseListData(uid: Int, token: String, title: String, unit: String) {
        val disposable = onlineHelpFormSubmissionModel.getHouseListData(uid, token, title, unit)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<HouseListData>>("获取房子的列表失败") {
                    override fun onNext(t: ArrayList<HouseListData>) {
                        mRootView?.setHouseListData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showError(e.mMessage, e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
    override fun getBlockListData(uid: Int, token: String) {
        checkViewAttached()
        var titleParam = ""
        val disposable = onlineHelpFormSubmissionModel.getBlockInfoData(uid, token)
//                .doOnNext {
//                    if (it.data != null) {
//                        mRootView?.setHouseListData(it.data)
//                    } else {
//                        mRootView?.showError(it.message, it.code)
//                    }
//                }
                .flatMap(Function<BaseResponse<ArrayList<BlockInfoData>>,Observable<BlockInfoData>>(){
                    if (it.data == null || it.data.isEmpty()) {
                        mRootView?.showError(it.message, it.code)
                        Observable.empty()
                    } else {
                        mRootView?.setBlockListData(it.data)
                        Observable.fromIterable(it.data)
                    }
                })
                .map(Function<BlockInfoData,String>(){
                    if (it == null) {
                        null
                    }else{
                     it.title
                    }
                })
                .flatMap(Function<String,Observable<BaseResponse<ArrayList<UnitListData>>>>(){
                    if (it == null ) {
                        Observable.empty()
                    } else {
                        titleParam = it
                        LogHelper.d("=========\ntitle\n$it")
                        onlineHelpFormSubmissionModel.getUnitListData(uid, token, it)
                    }
                })
//                .doOnNext(Consumer {
//                    if (it.data != null) {
//
//                    } else {
//                        mRootView?.showError(it.message, it.code)
//                    }
//                })
                .flatMap(Function<BaseResponse<ArrayList<UnitListData>>,Observable<UnitListData>>(){
                    if (it.data == null) {
                        mRootView?.showError(it.message, it.code)
                        Observable.empty()
                    } else {

                    Observable.fromIterable(it.data)
                    }
                })
                .map(Function<UnitListData,String>(){
                    if (it == null) {
                        null
                    }else{
                        it.unit
                    }
                })
                .flatMap(Function<String,Observable<BaseResponse<ArrayList<HouseListData>>>>(){
                    if (it == null) {
                        Observable.empty()
                    } else {
                    onlineHelpFormSubmissionModel.getHouseListData(uid, token, titleParam, it)
                    }
                })
                .subscribe {
                    if (it.data == null) {
                        mRootView?.showError(it.message, it.code)
                    } else {
//                        mRootView?.setHouseListData(it.data)
//                        val list = it.data
//                        list.forEach {
//                            val title = it.title
//                            onlineHelpFormSubmissionModel.getUnitListData(uid, token, title)
//                                    .subscribe {
//                                        if (it.data == null) {
//                                            mRootView?.showError(it.message, it.code)
//                                        } else {
//                                            val arrayList = it.data
//                                            for (i in 0 until arrayList.size){
//                                                onlineHelpFormSubmissionModel.getHouseListData(uid, token, title, arrayList[i].unit)
////                                                        .doOnNext(Consumer {
////                                                            LogHelper.d("============\n调用了")
////                                                        })
//                                            }
//                                        }
//                                    }
//                        }
                    }
                }
        addSubscription(disposable)


    }
    override fun getOnlineHelpFormSubmissionData(uid: RequestBody, token: RequestBody, type: RequestBody?, troubletype: RequestBody, is_online: RequestBody,
                                                 username: RequestBody, gender: RequestBody, phone: RequestBody, identity: RequestBody, address: RequestBody?,
                                                 title: RequestBody, content: RequestBody, parts: List<MultipartBody.Part>?) {
        checkViewAttached()
        val disposable = onlineHelpFormSubmissionModel.getOnlineHelpFormSubmissionData(uid, token, type, troubletype, is_online, username, gender,
                phone, identity, address,title, content, parts)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("提交失败"){

                    override fun onNext(t: Boolean) {
                        mRootView!!.setOnlineHelpFormSubmissionData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setOnlineHelpFormSubmissionData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}