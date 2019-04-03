package com.haidie.dangqun.ui.mine.activity

import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.MyReportContract
import com.haidie.dangqun.mvp.presenter.mine.MyReportPresenter
import com.haidie.dangqun.ui.mine.androidinterface.AndroidMyReportInterface
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.aop.CheckOnClick
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_multiple_web_view.*

/**
 * Create by   Administrator
 *      on     2018/10/24 10:59
 * description  我的上报
 */
class MyReportActivity : BaseActivity(), MyReportContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private val mPresenter by lazy { MyReportPresenter() }
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
        mPresenter.detachView()
    }
    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_MY_REPORT,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_MY_REPORT,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_MY_REPORT)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidMyReportInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        initWebViewSettings(mSettings)
    }
    override fun start() {
    }
    @CheckOnClick
    fun toEditMyReport(url : String){
//        跳转到编辑页面
        EditMyReportActivity.startActivity(this, url)
    }
    override fun reloadMyReport() {
        mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.RELOAD)
    }
    override fun showLoading() {
    }
    override fun dismissLoading() {
    }
}