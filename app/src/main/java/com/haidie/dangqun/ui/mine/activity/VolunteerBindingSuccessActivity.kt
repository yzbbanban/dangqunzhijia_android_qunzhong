package com.haidie.dangqun.ui.mine.activity

import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.activity.PointsMallListActivity
import com.haidie.dangqun.ui.home.activity.VolunteerActivitiesListActivity
import com.haidie.dangqun.ui.mine.androidinterface.AndroidVolunteerBindingSuccessInterface
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.StatusBarUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_multiple_web_view.*

/**
 * Create by   Administrator
 *      on     2018/10/23 11:27
 * description  志愿者详情
 */
class VolunteerBindingSuccessActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    override fun getLayoutId(): Int = R.layout.activity_multiple_web_view
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
    override fun initData() {
    }

    override fun initView() {
        StatusBarUtil.immersive(this)
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_VOLUNTEER_DETAIL,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_VOLUNTEER_DETAIL,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_VOLUNTEER_DETAIL)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidVolunteerBindingSuccessInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }

    override fun start() {
    }
    fun toPointsMallList(){
        //跳转到积分商城列表页面
        toActivity(PointsMallListActivity::class.java)
    }
    fun toVolunteerActivitiesList(){
        //跳转到志愿者活动列表页面
        toActivity(VolunteerActivitiesListActivity::class.java)
    }
}