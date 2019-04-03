package com.haidie.dangqun.ui.home.activity

import android.app.Activity
import android.content.Intent
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
 *      on     2018/11/30 09:09
 * description  智慧新风-活动公告、活动记录
 */
class EventAnnouncementsWebViewActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null
    companion object {
        private const val TYPE = "type"
        fun start(activity: Activity, type : Int){
            val intent = Intent(activity, EventAnnouncementsWebViewActivity::class.java)
            intent.putExtra(TYPE,type)
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
        val type = intent.getIntExtra(TYPE, -1)
        when (type) {
            Constants.EVENT_ANNOUNCEMENTS ->   url = UrlConstant.BASE_URL_EVENT_ANNOUNCEMENTS
            Constants.ACTIVITY_RECORD ->       url = UrlConstant.BASE_URL_ACTIVITY_RECORD
        }
    }

    override fun initView() {
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(url,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(url)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }

    override fun start() {
    }
}