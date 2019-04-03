package com.haidie.dangqun.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.widget.EditText
import android.widget.Toast
import com.classic.common.MultipleStatusView
import com.haidie.dangqun.R
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.utils.ActivityCollector
import com.haidie.dangqun.utils.aop.CheckOnClick
import com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.net.URLEncoder

/**
 * Created by admin2
 *  on 2018/08/09  10:13
 * description  BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 多种状态的 View 的切换
     */
    var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        ActivityCollector.instance.addActivity(this)
        initData()
        initView()
        start()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.instance.removeActivity(this)
    }
    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
    /**
     * 初始化数据
     */
    abstract fun initData()
    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()
    /**
     * 打开软键盘
     */
    fun openKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
    /**
     * 关闭软键盘
     */
    fun closeKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun toRequestBody(value : String): RequestBody = RequestBody.create(MediaType.parse("text/plain"),value)

    fun isBack(isBack: Boolean) {
        if (isBack) {
            finish()
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
        }
    }
    @CheckOnClick
    fun goToDetail(url: String) {
        LifeDetailActivity.startActivity(this, url)
    }
    private var myToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showShort( message: String){
        if (myToast == null) {
            myToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else {
            myToast!!.setText(message)
            myToast!!.duration = Toast.LENGTH_SHORT
        }
        myToast!!.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebViewSettings(mSettings: WebSettings) {
        mSettings.javaScriptEnabled = true
        mSettings.domStorageEnabled = true                      // 打开本地缓存提供JS调用,至关重要
        mSettings.allowFileAccess = true
        mSettings.setAppCacheEnabled(true)
        mSettings.databaseEnabled = true

        //缩放操作
        mSettings.setSupportZoom(false)  //支持缩放，默认为true。是下面那个的前提。
        mSettings.builtInZoomControls = false
        //不显示缩放按钮
        mSettings.displayZoomControls = false

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        mSettings.useWideViewPort = true        //将图片调整到适合WebView的大小
        mSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        mSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }
    fun toActivity(clazz : Class<*>){
        startActivity(Intent(this, clazz))
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
    }
    fun encode(str: String)= URLEncoder.encode(str,"UTF-8")!!

    fun syncCookie(url: String, cookie: String): Boolean {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setCookie(url, cookie)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
        }else{
            AsyncTask.THREAD_POOL_EXECUTOR.execute{ CookieManager.getInstance().flush() }
        }
        val newCookie = cookieManager.getCookie(url)
        return newCookie.isNotEmpty()
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    private fun getSmallBitmap(filePath: String?): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 600, 800)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }
    //把bitmap转换成String
     fun bitmapToString(filePath: String?): String {
        val bm = getSmallBitmap(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}