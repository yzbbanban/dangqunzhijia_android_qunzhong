package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VoluntaryOrganizationDetailContract
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationDetailData
import com.haidie.dangqun.mvp.presenter.home.VoluntaryOrganizationDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_voluntary_organization_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/11 15:56
 * description  志愿者组织详情
 */
class VoluntaryOrganizationDetailActivity : BaseActivity(),VoluntaryOrganizationDetailContract.View {

    private val mPresenter by lazy { VoluntaryOrganizationDetailPresenter() }
    private var mId: Int? = null
    private var title: String? = null
    private var isRefresh = false
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_voluntary_organization_detail

    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = title
        initWebView()
        mLayoutStatusView = multiple_status_view
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getVoluntaryOrganizationDetailData(uid,mId!!,token) }
    @SuppressLint("SetTextI18n")
    override fun setVoluntaryOrganizationDetailData(voluntaryOrganizationDetailData: VoluntaryOrganizationDetailData) {
        val isVolunteer = voluntaryOrganizationDetailData.is_volunteer
        frame_layout.visibility = if (isVolunteer == null) View.VISIBLE else View.GONE
        ll_wait.visibility = if (isVolunteer == null) View.GONE else View.VISIBLE
        tv_apply_join.visibility = if (frame_layout.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        val pic = voluntaryOrganizationDetailData.pic
        ImageLoader.load(this, pic, iv_pic)
        val title = voluntaryOrganizationDetailData.title
        tv_title_content.text = title
        val createTime = voluntaryOrganizationDetailData.create_time
        tv_create_time.text = createTime
        val man = voluntaryOrganizationDetailData.man
        tv_man.text = "$man"
        val woman = voluntaryOrganizationDetailData.woman
        tv_woman.text = "$woman"
        val activity = voluntaryOrganizationDetailData.activity
        tv_activity.text = "组织活动（$activity）"
        val totalNum = voluntaryOrganizationDetailData.total_num
        tv_total_num.text = "志愿者名单（$totalNum）"
        val waitMan = voluntaryOrganizationDetailData.wait_man
        tv_wait_man.text = "待审核志愿者（$waitMan）"
        val address = voluntaryOrganizationDetailData.address
        tv_address.text = address
        tv_time.text = createTime
        tv_num.text = "$totalNum"
        val username = voluntaryOrganizationDetailData.username
        tv_username.text = if (username.isEmpty()) "" else username
        val phone = voluntaryOrganizationDetailData.phone
        tv_phone.text = if (phone.isEmpty()) "" else phone
        val number = voluntaryOrganizationDetailData.number
        tv_number.text = number
        val url = Constants.HTML_BODY + voluntaryOrganizationDetailData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)

        ll_activity.setOnClickListener {
            //跳转到志愿者活动列表页面
            val intent = Intent(this@VoluntaryOrganizationDetailActivity, VolunteerActivitiesListActivity::class.java)
            intent.putExtra(Constants.ID, voluntaryOrganizationDetailData.id)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        ll_volunteer_list.setOnClickListener {
            //跳转到志愿者列表页面
            val intent = Intent(this@VoluntaryOrganizationDetailActivity, VolunteerListActivity::class.java)
            intent.putExtra(Constants.ID, voluntaryOrganizationDetailData.id)
//                    传入志愿者状态,默认传入2,表示志愿者,,如果是待审核志愿者,则传入0
            intent.putExtra(Constants.TYPE, if (isVolunteer == null) 0 else 2)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        ll_wait.setOnClickListener {
            //跳转到志愿者列表页面
            val intent = Intent(this@VoluntaryOrganizationDetailActivity, VolunteerListActivity::class.java)
            intent.putExtra(Constants.ID, voluntaryOrganizationDetailData.id)
//                    传入志愿者状态,默认传入2,表示志愿者,,如果是待审核志愿者,则传入0
            intent.putExtra(Constants.TYPE, 0)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        tv_apply_join.setOnClickListener {
            //跳转到志愿者申请页面
            val intent = Intent(this@VoluntaryOrganizationDetailActivity, VolunteerApplicationActivity::class.java)
            intent.putExtra(Constants.ID, voluntaryOrganizationDetailData.id)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
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
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
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
    override fun showRefreshEvent() { start() }
}