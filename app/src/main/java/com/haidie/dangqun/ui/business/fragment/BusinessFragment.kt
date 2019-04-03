package com.haidie.dangqun.ui.business.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.business.BusinessContract
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.mvp.presenter.business.BusinessPresenter
import com.haidie.dangqun.ui.business.androidinterface.AndroidBusinessListInterface
import com.haidie.dangqun.ui.business.view.CommodityClassificationPopupWindow
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.aop.CheckOnClick
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_commodity_trading.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by admin2
 *  on 2018/08/13  15:56
 * description  商圈
 */
class BusinessFragment : BaseFragment(), BusinessContract.View{

    private val mPresenter by lazy { BusinessPresenter() }

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var isRefresh = false
    private var mTitle: String? = null
    private var mAgentWeb: AgentWeb? = null
    private var commodityClassificationPopupWindow: CommodityClassificationPopupWindow? = null
    companion object {
        fun getInstance(title: String): BusinessFragment {
            val fragment = BusinessFragment()
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
        mPresenter.detachView()
    }
    override fun getLayoutId(): Int = R.layout.activity_commodity_trading

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        mPresenter.attachView(this)

        iv_search.setOnClickListener {
            clearFocus()
            search()
        }
        et_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                iv_clear.visibility = if (s!!.isEmpty()) View.INVISIBLE else View.VISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        et_search.setOnKeyListener {
            _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                closeKeyboard(et_search, activity)
                search()
            }
            false
        }
        iv_clear.setOnClickListener {
            et_search.text.clear()
        }
        ll_classification.setOnClickListener {
            clearFocus()
            //先调用商品分类列表接口，弹出分类选择窗口
            mPresenter.getCommodityClassificationListData(uid, token)
        }
        mLayoutStatusView = multipleStatusView
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_BUSINESS,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_BUSINESS,"${Constants.TOKEN}=$token")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout_business, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(activity!!,R.color.colorPrimary))
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_BUSINESS)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBusinessListInterface(this))
        val mWebView = mAgentWeb!!.webCreator.webView
        val mSettings = mWebView.settings

        val appCachePath = MyApplication.context.cacheDir.absolutePath
        mSettings.setAppCachePath(appCachePath)
        initWebViewSettings(mSettings)

        buildShowArrowAnimation()
        buildDismissArrowAnimation()
    }
    private fun search() {
        if (et_search.text.isEmpty()) {
            showShort("请输入搜索内容")
        } else { //搜索内容传入到JS
            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.CATEGORY, Constants.STRING_ZERO, "${et_search.text}")}
    }
    private fun clearFocus() {
        closeKeyboard(et_search, activity)
        et_search.clearFocus()
    }
    private lateinit var showArrowAnimation: RotateAnimation
    private fun buildShowArrowAnimation() {
        showArrowAnimation = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        showArrowAnimation.duration = 500
        showArrowAnimation.interpolator = AccelerateDecelerateInterpolator()
        showArrowAnimation.fillAfter = true
    }
    private lateinit var dismissArrowAnimation: RotateAnimation
    private fun buildDismissArrowAnimation() {
        dismissArrowAnimation = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        dismissArrowAnimation.duration = 500
        dismissArrowAnimation.interpolator = AccelerateDecelerateInterpolator()
        dismissArrowAnimation.fillAfter = true
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading()
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            dismissLoading()
            mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.SAVE_DATA,"$uid",token )
        }
    }
    override fun lazyLoad() {}
    override fun setCommodityClassificationListData(list: ArrayList<CommodityClassificationListData>) {
        //点击全部时显示全部内容
        list.add(0,CommodityClassificationListData("全部",0))
        commodityClassificationPopupWindow = CommodityClassificationPopupWindow(list,activity)
        commodityClassificationPopupWindow!!.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onBeforeDismiss(): Boolean {
                startDismissArrowAnimation()
                return super.onBeforeDismiss()
            }
            override fun onDismiss() {}
        }
        commodityClassificationPopupWindow!!.listener = object : CommodityClassificationPopupWindow.OnItemClickListener{
            override fun onClickListener(position: Int) {
                commodityClassificationPopupWindow!!.dismiss()
//                传值到JS刷新页面数据
                mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.CATEGORY,"${list[position].value}",Constants.EMPTY_STRING)
            }
        }
        //弹出分类选择窗口
        if (!commodityClassificationPopupWindow!!.isShowing) startShowArrowAnimation()
        commodityClassificationPopupWindow!!.showPopupWindow(linear_layout)
    }
    private fun startShowArrowAnimation() {
        iv_arrow.clearAnimation()
        iv_arrow.startAnimation(showArrowAnimation)
    }
    private fun startDismissArrowAnimation() {
        iv_arrow.clearAnimation()
        iv_arrow.startAnimation(dismissArrowAnimation)
    }
    override fun showError(msg: String, errorCode: Int) { showShort(msg) }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun reloadBusiness() { mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.RELOAD) }
    override fun dismissLoading() { mLayoutStatusView?.showContent() }

    @CheckOnClick
    fun goToBusinessDetail(url: String){
        LifeDetailActivity.startActivity(activity, url)
    }
}