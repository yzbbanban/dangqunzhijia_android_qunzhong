package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
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
 *      on     2018/11/29 10:32
 * description
 */
class HousekeepingDetailActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null
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
    companion object {
        fun start(context: FragmentActivity, url: String){
            val intent = Intent(context, HousekeepingDetailActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_multiple_web_view

    override fun initData() {
        url = intent.getStringExtra(Constants.URL_KEY)
    }

    override fun initView() {
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_HOST + url)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }

    override fun start() {
    }
}