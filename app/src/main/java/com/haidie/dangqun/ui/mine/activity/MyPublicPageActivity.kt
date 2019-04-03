package com.haidie.dangqun.ui.mine.activity

import android.app.Activity
import android.content.Intent
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
 *      on     2018/10/12 16:27
 * description  我的-收藏、粉丝、文章、商品、消息、关注
 */
class MyPublicPageActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null

    companion object {
        private const val MY_TYPE = "my_type"
        fun start(activity: Activity, type : Int){
            val intent = Intent(activity, MyPublicPageActivity::class.java)
            intent.putExtra(MY_TYPE,type)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
    }
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
    override fun initData() {
        val type = intent.getIntExtra(MY_TYPE, -1)
        when (type) {
            Constants.MY_TYPE_COLLECTION ->     url = UrlConstant.BASE_URL_MY_COLLECTION
            Constants.MY_TYPE_FANS ->           url = UrlConstant.BASE_URL_MY_FANS
            Constants.MY_TYPE_ARTICLE ->        url = UrlConstant.BASE_URL_MY_ARTICLE
            Constants.MY_TYPE_PRODUCT ->        url = UrlConstant.BASE_URL_MY_PRODUCT
            Constants.MY_TYPE_MESSAGE ->        url = UrlConstant.BASE_URL_MY_MESSAGE
            Constants.MY_TYPE_ATTENTION ->      url = UrlConstant.BASE_URL_MY_ATTENTION
        }
    }
    override fun initView() {
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(url,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(url)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.SAVE_DATA,"$uid",token )
        }
    }
    override fun start() {}
}