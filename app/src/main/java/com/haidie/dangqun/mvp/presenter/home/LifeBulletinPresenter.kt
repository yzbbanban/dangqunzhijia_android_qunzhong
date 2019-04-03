package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.LifeBulletinContract
import com.haidie.dangqun.mvp.model.bean.ArticleListData
import com.haidie.dangqun.mvp.model.home.LifeBulletinModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/08 17:26
 * description
 */
class LifeBulletinPresenter : BasePresenter<LifeBulletinContract.View>(), LifeBulletinContract.Presenter {
    private val lifeBulletinModel by lazy { LifeBulletinModel() }
    override fun getArticleListData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = lifeBulletinModel.getArticleListData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArticleListData>("获取生活公告列表失败"){
                    override fun onNext(t: ArticleListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setArticleListData(t)
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