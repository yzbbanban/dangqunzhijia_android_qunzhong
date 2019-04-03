package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData
import com.haidie.dangqun.mvp.presenter.home.VolunteerActivitiesDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_volunteer_activities_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/11 13:25
 * description  志愿者活动详情
 */
class VolunteerActivitiesDetailActivity : BaseActivity(),VolunteerActivitiesDetailContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mId: Int? = null
    private var isRefresh = false
    private val mPresenter by lazy { VolunteerActivitiesDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_volunteer_activities_detail

    override fun initData() { mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE) }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "详细信息"

        initWebView()
        mLayoutStatusView = multiple_status_view
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() { mPresenter.getVolunteerActivitiesDetailData(uid,mId!!,token) }
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
    @SuppressLint("SetTextI18n")
    override fun setVolunteerActivitiesDetailData(volunteerActivitiesDetailData: VolunteerActivitiesDetailData) {
        tv_activities_title.text = volunteerActivitiesDetailData.title
        val area = volunteerActivitiesDetailData.area
        val address = volunteerActivitiesDetailData.address
        tv_area_address.text = area + address
        val startTime = volunteerActivitiesDetailData.start_time
        val endTime = volunteerActivitiesDetailData.end_time
        tv_time.text = DateUtils.getTimeToChina(startTime) + "-" + DateUtils.getTimeToChina(endTime)
        val num = volunteerActivitiesDetailData.num
        tv_num.text = "已报名$num"
        val needNum = volunteerActivitiesDetailData.need_num
        tv_need_num.text = "/需" + needNum + "人"
        val url = Constants.HTML_BODY + volunteerActivitiesDetailData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        val isJoin = volunteerActivitiesDetailData.is_join
        if (isJoin == null) {
            frame_layout.visibility = View.VISIBLE
            tv_add_activity.visibility = View.VISIBLE
        } else {
            linear_layout_.visibility = View.VISIBLE
            text_view_.visibility = View.VISIBLE
            tv_sign_in.visibility = View.VISIBLE
        }

        linear_layout.setOnClickListener {
//            跳转到志愿者参加活动人员列表页面
            val intent = Intent(this@VolunteerActivitiesDetailActivity, VolunteersParticipateActivitiesActivity::class.java)
            intent.putExtra(Constants.ID, volunteerActivitiesDetailData.id)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        tv_add_activity.setOnClickListener {
            CircularAnim.hide(tv_add_activity)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/activity/addActivity
            val nowTime = DateUtils.getNowTime()
            if (DateUtils.isDateOneBigger(nowTime, endTime)) {
                showAddActivity()
                showShort("活动已结束")
                return@setOnClickListener
            }
            mPresenter.getAddActivityData(uid, mId!!, token)
        }
        //http://192.168.3.3/dangqun_backend_mayun/public/api/activity/signin
        tv_sign_in.setOnClickListener {  mPresenter.getSignInData(uid,mId!!,token) }
    }
    override fun setAddActivityData(isSuccess: Boolean, msg: String, errorCode: Int) {
        if (isSuccess) {
            showShort("报名成功")
            //返回到上级页面
            onBackPressed()
        } else {
            showShort(msg)
            showAddActivity()
        }
    }
    override fun setSignInData(isSuccess: Boolean,msg : String) { showShort(msg) }
    private fun showAddActivity() {
        CircularAnim.show(tv_add_activity).go()
        progress_bar.visibility = View.GONE
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
    override fun setVolunteerActivitiesSignInData(isSuccess: Boolean, msg: String,errorCode : Int) {
    }
}