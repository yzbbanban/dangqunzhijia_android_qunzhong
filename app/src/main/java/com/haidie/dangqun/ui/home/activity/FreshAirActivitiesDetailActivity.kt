package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.net.http.SslError
import android.view.View
import android.webkit.*
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.FreshAirActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesDetailData
import com.haidie.dangqun.mvp.presenter.home.FreshAirActivitiesDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_fresh_air_activities_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/10 14:50
 * description 活动公告-新风活动-详情
 */
class FreshAirActivitiesDetailActivity : BaseActivity(),FreshAirActivitiesDetailContract.View {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { FreshAirActivitiesDetailPresenter() }
    private var mId: Int? = null
    private var isRefresh = false
    override fun getLayoutId(): Int = R.layout.activity_fresh_air_activities_detail

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "活动详情"
        mLayoutStatusView = multipleStatusView
        initWebView()
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.pluginState = WebSettings.PluginState.ON
        // 设置支持缩放
        webSettings.builtInZoomControls = true
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.setSupportZoom(true)
        webSettings.textZoom = 250
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true
        webSettings.blockNetworkImage = true
        webSettings.setSupportMultipleWindows(true)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        webSettings.useWideViewPort = true        //将图片调整到适合WebView的大小
        webSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.webViewClient = CustomWebViewClient()
        webSettings.setAppCacheEnabled(true)
    }
    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            // 接受所有网站的证书，忽略SSL错误，执行访问网页  
            handler?.proceed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getFreshAirActivitiesDetailData(uid,token,mId!!)
    }
    @SuppressLint("SetTextI18n")
    override fun setFreshAirActivitiesDetailData(freshAirActivitiesDetailData: FreshAirActivitiesDetailData) {
        tvTitle.text = freshAirActivitiesDetailData.title
        if (freshAirActivitiesDetailData.content.isNotEmpty()) {
            val url = Constants.HTML_BODY + freshAirActivitiesDetailData.content + Constants.BODY_HTML
            webView.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        }
        tvTime.text = "${freshAirActivitiesDetailData.start_time.split(" ")[0]}至${freshAirActivitiesDetailData.end_time.split(" ")[0]}"
        tvManager.text = freshAirActivitiesDetailData.manager
        tvContact.text = freshAirActivitiesDetailData.contact
        tvActivityName.text = freshAirActivitiesDetailData.activity_name
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR ->   mLayoutStatusView?.showNoNetwork()
            else ->   mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}