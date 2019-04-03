package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.NewPaymentAccountContract
import com.haidie.dangqun.mvp.model.bean.*
import com.haidie.dangqun.mvp.model.home.NewPaymentAccountModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/27 10:06
 * description
 */
class NewPaymentAccountPresenter : BasePresenter<NewPaymentAccountContract.View>(),NewPaymentAccountContract.Presenter{

    private val newPaymentAccountModel by lazy { NewPaymentAccountModel() }
    override fun getBuildingUnitAndRoomNumberData(uid: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = newPaymentAccountModel.getBuildingUnitAndRoomNumberData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<BuildingUnitAndRoomNumberData>("获取楼栋、单元和房间编号失败") {
                    override fun onNext(t: BuildingUnitAndRoomNumberData) {
                        mRootView?.apply {
                            dismissLoading()
                            setBuildingUnitAndRoomNumberData(t)
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

    override fun getBlockInfoData(uid: Int, token: String) {
        val disposable = newPaymentAccountModel.getBlockInfoData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<BlockInfoData>>("获取楼栋列表失败") {
                    override fun onNext(t: ArrayList<BlockInfoData>) {
                        mRootView?.setBlockInfoData(t, "")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.setBlockInfoData(null, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
    override fun getUnitListData(uid: Int, token: String, title: String) {
        val disposable = newPaymentAccountModel.getUnitListData(uid, token, title)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<UnitListData>>("获取单元的列表失败") {
                    override fun onNext(t: ArrayList<UnitListData>) {
                        mRootView?.setUnitListData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setUnitListData(null, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
    override fun getHouseListData(uid: Int, token: String, title: String, unit: String) {
        val disposable = newPaymentAccountModel.getHouseListData(uid, token, title, unit)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<HouseListData>>("获取房子的列表失败") {
                    override fun onNext(t: ArrayList<HouseListData>) {
                        mRootView?.setHouseListData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setHouseListData(null, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
    override fun getRoomNumberUserInfoData(uid: Int, token: String, house_id: String) {
        val disposable = newPaymentAccountModel.getRoomNumberUserInfoData(uid, token, house_id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<RoomNumberUserInfoData>("获取业主姓名,手机号信息失败") {

                    override fun onNext(t: RoomNumberUserInfoData) {
                        mRootView?.setRoomNumberUserInfoData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setRoomNumberUserInfoData(null, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }

    override fun getAddPaymentAccountResultData(uid: Int, token: String, house_id: Int) {
        val disposable = newPaymentAccountModel.getAddPaymentAccountResultData(uid, token, house_id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("新增失败") {
                    override fun onNext(t: Boolean) {
                        mRootView?.setAddPaymentAccountResultData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setAddPaymentAccountResultData(false, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}