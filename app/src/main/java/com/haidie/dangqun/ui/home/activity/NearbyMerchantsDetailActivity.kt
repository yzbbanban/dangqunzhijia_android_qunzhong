package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.androidinterface.AndroidNearbyMerchantsDetailInterface
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.StatusBarUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_multiple_web_view.*

/**
 * Create by   Administrator
 *      on     2018/10/16 15:30
 * description  附近商家详情
 */
class NearbyMerchantsDetailActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private var mWebView: WebView? = null
    private var url: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var addressStr: String? = null
    companion object {
        fun start(context: FragmentActivity, url: String, latitude: String, longitude: String, addressStr: String){
            val intent = Intent(context, NearbyMerchantsDetailActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            intent.putExtra(Constants.LATITUDE, latitude)
            intent.putExtra(Constants.LONGITUDE, longitude)
            intent.putExtra(Constants.ADDRESS_STR, addressStr)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
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
        url = intent.getStringExtra(Constants.URL_KEY)
        latitude = intent.getStringExtra(Constants.LATITUDE)
        longitude = intent.getStringExtra(Constants.LONGITUDE)
        addressStr = intent.getStringExtra(Constants.ADDRESS_STR)
    }

    override fun initView() {
        StatusBarUtil.immersive(window)
        mLayoutStatusView = multiple_status_view
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.LATITUDE}=$latitude")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.LONGITUDE}=$longitude")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.LONGITUDE}=$longitude")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.ADDRESS_STR}=${encode(addressStr!!)}")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_HOST + url)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidNearbyMerchantsDetailInterface(this))
        mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView!!.settings
        initWebViewSettings(mSettings)
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.SAVE_DATA,"$uid",token )
        }
    }
    override fun start() {
    }
    fun toEvaluationList(url : String){
//        跳转到附近商家-评价列表页面
        NearbyMerchantsEvaluationListActivity.start(this,url)
    }
    fun toEvaluation(url : String){
//        跳转到附近商家-评价页面
        NearbyMerchantsEvaluationActivity.start(this,url)
    }
    fun toPicturePreview(url: Array<String>){
        val localMedia = ArrayList<LocalMedia>()
        url.forEach {
            val localMedia1 = LocalMedia()
            localMedia1.path = UrlConstant.BASE_URL_HOST + it
            localMedia.add(localMedia1)
        }
        PictureSelector.create(this@NearbyMerchantsDetailActivity)
                .themeStyle(R.style.picture_default_style)
                .openExternalPreview(0, localMedia)
    }
}