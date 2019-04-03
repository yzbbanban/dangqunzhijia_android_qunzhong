package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.VolunteerBindingContract
import com.haidie.dangqun.mvp.model.bean.VolunteerInfoData
import com.haidie.dangqun.mvp.presenter.mine.VolunteerBindingPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.PointsMallListActivity
import com.haidie.dangqun.ui.home.activity.VolunteerActivitiesListActivity
import com.haidie.dangqun.ui.mine.androidinterface.AndroidVolunteerBindingInterface
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_volunteer_binding.*

/**
 * Create by   Administrator
 *      on     2018/10/19 16:56
 * description  志愿者绑定
 */
class VolunteerBindingActivity : BaseActivity(),VolunteerBindingContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { VolunteerBindingPresenter() }
    private var mWebView: WebView? = null
    override fun getLayoutId(): Int = R.layout.activity_volunteer_binding

    override fun onDestroy() {
        super.onDestroy()
        mWebView?.let {
            it.destroy()
            null
        }
    }
    override fun initData() {}

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView
        mWebView = webView
        var mSettings : WebSettings? = null
        mWebView?.let {
            it.webViewClient = mWebViewClient
            mSettings = it.settings
        }
        initWebViewSettings(mSettings!!)
        webView.addJavascriptInterface(AndroidVolunteerBindingInterface(this@VolunteerBindingActivity), Constants.ANDROID)
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mLayoutStatusView?.showContent()
        }
    }
    override fun start() {
//        先调用获取志愿者基本信息判断是否为志愿者
        mPresenter.getVolunteerInfoData(uid, token)
    }
    override fun setVolunteerInfoData(volunteerInfoData: VolunteerInfoData) {
//        是志愿者,加载志愿者详情页面
        StatusBarUtil.immersive(this)
        syncCookie(UrlConstant.BASE_URL_VOLUNTEER_DETAIL,"${Constants.UID}=$uid")
        syncCookie(UrlConstant.BASE_URL_VOLUNTEER_DETAIL,"${Constants.TOKEN}=$token")
        mWebView?.loadUrl(UrlConstant.BASE_URL_VOLUNTEER_DETAIL)
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
//            不是志愿者,加载绑定页面
            201 ->  {
                syncCookie(UrlConstant.BASE_URL_VOLUNTEER_BIND,"${Constants.UID}=$uid")
                syncCookie(UrlConstant.BASE_URL_VOLUNTEER_BIND,"${Constants.TOKEN}=$token")
                mWebView?.loadUrl(UrlConstant.BASE_URL_VOLUNTEER_BIND)
            }
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }
    override fun dismissLoading() {}
    fun toPointsMallList(){
        //跳转到积分商城列表页面
        toActivity(PointsMallListActivity::class.java)
    }
    fun toVolunteerActivitiesList(){
        //跳转到志愿者活动列表页面
        toActivity(VolunteerActivitiesListActivity::class.java)
    }
    fun bindingSuccess(isSuccess : Boolean){
//        绑定成功加载志愿者详情页面
        if (isSuccess) {
            showShort("绑定成功")
            //跳转到绑定成功页面
            startActivity(Intent(this, VolunteerBindingSuccessActivity::class.java))
            finish()
        }else{
            showShort("绑定失败")
        }
    }
}