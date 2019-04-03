package com.haidie.dangqun.ui.mine.activity

import android.support.v4.content.ContextCompat
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.mine.androidinterface.AndroidBackDetailInterface
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_multiple_web_view.*

/**
 * Create by   Administrator
 *      on     2018/09/13 10:06
 * description  我的消息
 */
class MyMessageActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    override fun onPause() {
        mAgentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }
    override fun onResume() {
        mAgentWeb!!.webLifeCycle.onResume()
        super.onResume()
    }
    override fun onDestroy() {
        mAgentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }
    override fun getLayoutId(): Int = R.layout.activity_multiple_web_view
    override fun initData() {}
    override fun initView() {
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_MY_MESSAGE,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_MY_MESSAGE,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_MY_MESSAGE)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.SAVE_DATA,
                    "$uid",token )
        }
    }
    override fun start() {}
}