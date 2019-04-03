package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.event.ReloadFaultRepairEvent
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.home.androidinterface.AndroidReleaseWorkOrderInterface
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_frame_layout.*

/**
 * Create by   Administrator
 *      on     2018/09/22 12:22
 * description  发布工单
 */
class ReleaseWorkOrderActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mUploadCallBackAbove: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val fileChooserResultCode = 2
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null
    companion object {
        fun startActivity(context: FragmentActivity, url: String) {
            val intent = Intent(context, ReleaseWorkOrderActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_frame_layout
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
    }
    override fun initView() {
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(Constants.NEGATIVE_ONE, Constants.NEGATIVE_ONE))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setWebChromeClient(mWebChromeClient)
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_HOST + url)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidReleaseWorkOrderInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings
        val appCachePath = MyApplication.context.cacheDir.absolutePath
        mSettings.setAppCachePath(appCachePath)
        initWebViewSettings(mSettings)
    }
    private var mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        //For Android 5.0
        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
            mUploadCallBackAbove = filePathCallback
            take()
            return true
        }
        //  android 4.0 - android 4.3  安卓4.4.4也用的这个方法
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            mUploadMessage = uploadMsg
            take()
        }
    }
    private fun take() {
        val albumIntent = Intent(Intent.ACTION_PICK, null)
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE)
        startActivityForResult(albumIntent, fileChooserResultCode)
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
    override fun start() {}
    fun handleJSEvent(code : Int){
        when (code) {
        // code = 1 该页面取消，返回上一级页面
            1 -> {
                finish()
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
            }
        //  code = 2 发布失败，停留在本页面
            2 -> showShort("发布失败")
        }
    }
    fun releaseSuccess(){
        showShort("发布成功")
        // 刷新故障报修
        RxBus.getDefault().post(ReloadFaultRepairEvent())
        finish()
    }
}