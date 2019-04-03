package com.haidie.dangqun.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.widget.EditText
import android.widget.Toast
import com.classic.common.MultipleStatusView
import com.haidie.dangqun.R
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.utils.aop.CheckOnClick


abstract class BaseFragment: Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载
     */
    private var hasLoadData = false
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),null)
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnRetryClickListener(mRetryClickListener)
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open var isReload = false
    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        isReload = true
        lazyLoad()
    }
    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId():Int
    /**
     * 初始化 View
     */
    abstract fun initView()
    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    /**
     * 关闭软键盘
     */
    fun closeKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
    private var myToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showShort( message: String){
        if (myToast == null) {
            myToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
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
        mSettings.useWideViewPort = true        //将图片调整到适合webView的大小
        mSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        mSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }
    @CheckOnClick
    fun goToDetail(url: String) {
        LifeDetailActivity.startActivity(activity, url)
    }
    fun toActivity(clazz : Class<*>){
        startActivity(Intent(activity, clazz))
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
    }
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
}
