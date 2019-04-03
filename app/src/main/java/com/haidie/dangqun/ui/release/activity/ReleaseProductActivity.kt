package com.haidie.dangqun.ui.release.activity

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.event.ReloadBusinessEvent
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.main.activity.MainActivity
import com.haidie.dangqun.ui.release.androidinterface.AndroidReleaseProductInterface
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_release_product.*



/**
 * Create by   Administrator
 *      on     2018/09/03 10:16
 * description  发布商品
 */
class ReleaseProductActivity : BaseActivity() {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mUploadCallBackAbove: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val fileChooserResultCode = 2
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
    override fun getLayoutId(): Int = R.layout.activity_release_product
    override fun initData() {}

    override fun initView() {
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_RELEASE_PRODUCT,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_RELEASE_PRODUCT,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout_release_product, LinearLayout.LayoutParams(Constants.NEGATIVE_ONE, Constants.NEGATIVE_ONE))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_RELEASE_PRODUCT)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidReleaseProductInterface(this))
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
                    "$uid", token)
        }
    }
    private var mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        //For Android 5.0
        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
            mUploadCallBackAbove = filePathCallback
            take()
            return true
        }
        //For Android 4.1
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            mUploadMessage = uploadMsg
            take()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileChooserResultCode) {
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            when {
                mUploadCallBackAbove != null -> onActivityResultAbove(requestCode, resultCode, data)
                mUploadMessage != null -> {
                    mUploadMessage!!.onReceiveValue(result)
                    mUploadMessage = null
                }
            }
        }
    }

    private fun onActivityResultAbove(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != fileChooserResultCode || mUploadCallBackAbove == null) {
            return
        }
        var results: Array<Uri>? = null
        if (resultCode == RESULT_OK) {
            if (data != null) {
                val dataString = data.dataString
                val clipData = data.clipData
                if (clipData != null) {
                    results = arrayOf()
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mUploadCallBackAbove!!.let {
            it.onReceiveValue(results)
             null
        }
    }
    private fun take() {
        val albumIntent = Intent(Intent.ACTION_PICK, null)
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE)
        startActivityForResult(albumIntent, fileChooserResultCode)
    }
    override fun start() {}
    fun handleJSEvent(code : Int){
        when (code) {
        // code = 1 该页面取消，返回上一级页面
            1 -> finish()
        //  code = 2 发布失败，停留在本页面
            2 -> {
                showShort("发布失败")
            }
        }
    }
    fun releaseSuccess(){
        showShort("发布成功")
        // 刷新商圈数据
        RxBus.getDefault().post(ReloadBusinessEvent())
        val intent = Intent(this@ReleaseProductActivity, MainActivity::class.java)
        intent.putExtra(Constants.TAB,1)
        startActivity(intent)
    }
}