package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.HomeContract
import com.haidie.dangqun.mvp.model.bean.HomeNewsData
import com.haidie.dangqun.mvp.model.home.HomeModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils


/**
 * Created by admin2
 *  on 2018/08/10  10:49
 * description
 */
class HomePresenter : BasePresenter<HomeContract.View>() , HomeContract.Presenter {

    private val homeModel by lazy { HomeModel() }
    override fun getHomeData(user_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeModel.getHomeBannerData(user_id, token)
                .doOnNext{
                    mRootView?.apply {
                        when {
                            it.code == ApiErrorCode.SUCCESS -> setHomeBannerData(it.data!!)
                            else -> showError(it.message,it.code)
                        }
                    }
                }
                .flatMap{homeModel.getHomeNewsData(user_id, token)}
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HomeNewsData>("获取首页新闻数据失败"){

                    override fun onNext(t: HomeNewsData) {
                        mRootView?.apply {
                            dismissLoading()
                            setHomeNewsData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
}