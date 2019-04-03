package com.haidie.dangqun.ui.business.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_business_list.*

/**
 * Create by   Administrator
 *      on     2018/09/03 11:11
 * description  商圈列表
 */
class BusinessListFragment : BaseFragment() {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mTitle: String? = null
    private var mAgentWeb: AgentWeb? = null
    companion object {
        fun getInstance(title: String): BusinessListFragment {
            val fragment = BusinessListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
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
    override fun getLayoutId(): Int = R.layout.fragment_business_list

    override fun initView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout_business, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(activity!!,R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_BUSINESS)
//        mAgentWeb!!.jsInterfaceHolder.addJavaObject("android", AndroidBusinessListInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings

        val appCachePath = MyApplication.context.cacheDir.absolutePath
        mSettings.setAppCachePath(appCachePath)
        initWebViewSettings(mSettings)
    }

    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.SAVE_DATA,
                    "$uid",token )
        }
    }
    override fun lazyLoad() {}

    fun goToBusinessDetail(url: String){
        LifeDetailActivity.startActivity(activity, url)
    }
}