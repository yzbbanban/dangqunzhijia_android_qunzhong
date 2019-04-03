package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.ServiceDetailContract
import com.haidie.dangqun.mvp.model.bean.ServiceDetailItemData
import com.haidie.dangqun.mvp.presenter.home.ServiceDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import kotlinx.android.synthetic.main.activity_service_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*



/**
 * Create by   Administrator
 *      on     2018/09/10 12:51
 * description  服务详情页
 */
class ServiceDetailActivity : BaseActivity(),ServiceDetailContract.View {
    private var mId: String? = null
    private var title: String? = null
    private var phone: String? = null
    private var isRefresh = false
    private val mPresenter by lazy { ServiceDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_service_detail
    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        mId = intent.getStringExtra(Constants.ID)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {  onBackPressed() }
        tv_title.text = title
        mLayoutStatusView = multiple_status_view
        initWebView()
        tv_phone.setOnClickListener {
            call(phone!!)
        }
        linear_layout.setOnClickListener {
            call(phone!!)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() { mPresenter.getServiceDetailData(mId!!) }
    @SuppressLint("SetTextI18n")
    override fun setServiceDetailData(list: List<ServiceDetailItemData>) {
        val serviceDetailItemData = list[0]
        tv_title_content.text = serviceDetailItemData.title
        tv_create_time.text = "发布时间      " + serviceDetailItemData.create_time
        tv_category.text = serviceDetailItemData.category
        tv_area.text = serviceDetailItemData.area
        tv_name.text = serviceDetailItemData.name
        tv_address.text = serviceDetailItemData.address

        val url = Constants.HTML_BODY + serviceDetailItemData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        phone = serviceDetailItemData.phone

        tv_online_order.setOnClickListener {
            //跳转到在线下单页面
            val intent = Intent(this@ServiceDetailActivity, OnlineOrderActivity::class.java)
            intent.putExtra(Constants.TEXT, tv_online_order.text.toString())
            intent.putExtra(Constants.ID, serviceDetailItemData.id)
            startActivity(intent)
        }
    }
    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    private fun initWebView() {
        val webSettings = web_view.settings
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        webSettings.textZoom = 250
        webSettings.setSupportZoom(false)  //支持缩放，默认为true
        //不显示缩放按钮
        webSettings.displayZoomControls = false

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        webSettings.useWideViewPort = true        //将图片调整到适合WebView的大小
        webSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        web_view.webViewClient = WebViewClient()
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {  mLayoutStatusView?.showContent() }
}