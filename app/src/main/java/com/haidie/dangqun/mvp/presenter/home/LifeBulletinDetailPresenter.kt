package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.LifeBulletinDetailContract
import com.haidie.dangqun.mvp.model.bean.ActivityDetailData
import com.haidie.dangqun.mvp.model.bean.ArticleDetailData
import com.haidie.dangqun.mvp.model.home.LifeBulletinDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 09:56
 * description
 */
class LifeBulletinDetailPresenter : BasePresenter<LifeBulletinDetailContract.View>(),LifeBulletinDetailContract.Presenter{

    private val lifeBulletinDetailModel by lazy { LifeBulletinDetailModel() }
    override fun getArticleDetailData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = lifeBulletinDetailModel.getArticleDetailData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<List<ArticleDetailData>>("获取文章详情失败"){
                    override fun onNext(t: List<ArticleDetailData>) {
                        mRootView!!.apply {
                            dismissLoading()
                            setArticleDetailData(t)
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

    override fun getActivityDetailData(id: String, uid: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = lifeBulletinDetailModel.getActivityDetailData(id, uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<List<ActivityDetailData>>("获取活动详情失败"){
                    override fun onNext(t: List<ActivityDetailData>) {
                        mRootView!!.apply {
                            dismissLoading()
                            setActivityDetailData(t[0])
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
    override fun getAddActivityData(uid: Int, id: Int, token: String) {
        val disposable = lifeBulletinDetailModel.getAddActivityData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("报名失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setAddActivityData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setAddActivityData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}