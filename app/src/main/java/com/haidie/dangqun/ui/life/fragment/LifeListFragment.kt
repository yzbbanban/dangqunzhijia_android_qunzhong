package com.haidie.dangqun.ui.life.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.life.LifeListContract
import com.haidie.dangqun.mvp.presenter.life.LifeListPresenter
import com.haidie.dangqun.ui.life.androidinterface.AndroidLifeListInterface
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.fragment_life_list.*

/**
 * Created by admin2
 *  on 2018/08/13  20:09
 * description 生活列表
 */
class LifeListFragment : BaseFragment(),LifeListContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var index: Int = 0
    private var isOfficial: String = "0"
    private val mPresenter by lazy { LifeListPresenter() }
    companion object {
        fun getInstance(position: Int): LifeListFragment {
            val fragment = LifeListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.index = position
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun getLayoutId(): Int = R.layout.fragment_life_list
    override fun initView() {
        mPresenter.attachView(this)
        when (index) {
            0,1 -> isOfficial = "0"
            2,3 -> isOfficial = "1"
        }
        mLayoutStatusView = multiple_status_view

        val mSettings  = webView.settings
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.UID}=$uid")
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.TOKEN}=$token")
        webView.webViewClient = mWebViewClient
        webView.addJavascriptInterface(AndroidLifeListInterface(this@LifeListFragment),Constants.ANDROID)
        initWebViewSettings(mSettings)
    }
    override fun lazyLoad() {
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.TAB}=$index")
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.IS_OFFICIAL}=$isOfficial")
        webView.loadUrl(UrlConstant.BASE_URL_LIFE)
    }
    private var mIsRedirect: Boolean = false
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView,  url :String): Boolean {
            mIsRedirect = true
            view.loadUrl(url)
            return true
        }
        override fun onPageStarted(view: WebView,  url :String,  favicon : Bitmap?) {
            mIsRedirect = false
            super.onPageStarted(view, url, favicon)
        }
        override fun onPageFinished(view: WebView,  url :String) {
            super.onPageFinished(view, url)
            if (mIsRedirect) {
                return
            }
        }
    }

    override fun reloadLife() {
//        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.UID}=$uid")
//        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.TOKEN}=$token")
//        webView.post {
//            webView.evaluateJavascript("javascript:${Constants.RELOAD}()") {}
//        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}