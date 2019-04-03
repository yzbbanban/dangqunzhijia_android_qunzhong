package com.haidie.dangqun.ui.release.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
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
import com.haidie.dangqun.mvp.event.ReloadLifeEvent
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.main.activity.MainActivity
import com.haidie.dangqun.ui.release.androidinterface.AndroidReleaseArticleInterface
import com.haidie.dangqun.ui.release.androidinterface.AndroidReleaseArticleInterface.Companion.mCurrentPhotoPath
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_release_article.*
import java.io.File

/**
 * Created by admin2
 *  on 2018/08/21  11:50
 * description  发布文章
 */
class ReleaseArticleActivity : BaseActivity() {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mUploadCallBackAbove: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val takePhotoResultCode = 1
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
    override fun getLayoutId(): Int = R.layout.activity_release_article

    override fun initData() {}
    override fun initView() {
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_RELEASE_ARTICLE,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_RELEASE_ARTICLE,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout_release_article, LinearLayout.LayoutParams(Constants.NEGATIVE_ONE, Constants.NEGATIVE_ONE))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_RELEASE_ARTICLE)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidReleaseArticleInterface(this))
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
//       android 4.0 - android 4.3  安卓4.4.4也用的这个方法
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
        }//因为拍照指定了路径，所以data值为null
        else if (requestCode == takePhotoResultCode) {
            val pictureFile = File(AndroidReleaseArticleInterface.mCurrentPhotoPath)
            val uri = Uri.fromFile(pictureFile)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = uri
            sendBroadcast(intent)  // 这里我们发送广播让MediaScanner 扫描我们制定的文件
            // 这样在系统的相册中我们就可以找到我们拍摄的照片了【但是这样一来，就会执行MediaScanner服务中onLoadFinished方法，所以需要注意】
            try {
                val string = bitmapToString(AndroidReleaseArticleInterface.mCurrentPhotoPath)
                setPlatformType(string)
            } catch (e: Exception) {
//                捕捉部分手机返回null的情况
                LogHelper.d("=====\n$e")
                //android调用H5代码
                mAgentWeb!!.webCreator.webView.loadUrl("javascript:cancel()")
            }
        }
    }

    private fun setPlatformType(result: String?) {
        //android调用H5代码
        mAgentWeb!!.webCreator.webView.loadUrl("javascript:cameraResult('data:image/jpg;base64,$result')")
        mAgentWeb!!.webCreator.webView.loadUrl("javascript:cancel()")
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
    fun takePicture() {
        //调用系统拍照
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            //解决buildSdk>=24,调用Uri.fromFile时报错的问题
            // https://blog.csdn.net/qq_34709056/article/details/77968456
            //https://blog.csdn.net/qq_34709056/article/details/78528507
            mCurrentPhotoPath = "${Constants.PATH_PIC}Pictures" +
                    File.separator + "JPEG_" + System.currentTimeMillis() + ".jpg"
            val file = File(mCurrentPhotoPath)
            val photoFile: Uri?
            photoFile = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = applicationInfo.packageName + ".provider"
                FileProvider.getUriForFile(applicationContext, authority, file)
            } else {
                Uri.fromFile(file)
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            //启动拍照的窗体。并注册 回调处理
            startActivityForResult(takePictureIntent, takePhotoResultCode)
        }
    }
    fun choosePicture(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" // 查看类型
        startActivityForResult(intent, fileChooserResultCode)
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
        // 刷新生活数据
        RxBus.getDefault().post(ReloadLifeEvent())
        val intent = Intent(this@ReleaseArticleActivity, MainActivity::class.java)
        intent.putExtra(Constants.TAB,2)
        startActivity(intent)
    }
}