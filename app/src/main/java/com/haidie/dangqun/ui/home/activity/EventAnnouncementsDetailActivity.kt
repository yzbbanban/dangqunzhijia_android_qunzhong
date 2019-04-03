package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData
import com.haidie.dangqun.mvp.presenter.home.VolunteerActivitiesDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_event_announcements_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/28 13:23
 * description  公告详情
 */
class EventAnnouncementsDetailActivity : BaseActivity(), VolunteerActivitiesDetailContract.View {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { VolunteerActivitiesDetailPresenter() }
    private var mId: Int? = null
    private var isRefresh = false
    override fun getLayoutId(): Int = R.layout.activity_event_announcements_detail

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "公告详情"
        mLayoutStatusView = multipleStatusView
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getVolunteerActivitiesDetailData(uid,mId!!,token)
    }
    override fun setVolunteerActivitiesDetailData(volunteerActivitiesDetailData: VolunteerActivitiesDetailData) {
        val pic = volunteerActivitiesDetailData.pic
        ImageLoader.load(this,pic,ivPic)
        val title = volunteerActivitiesDetailData.title
        tvTitle.text = title
        val url = Constants.HTML_BODY + volunteerActivitiesDetailData.content + Constants.BODY_HTML
        webView.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        val area = volunteerActivitiesDetailData.area
        val address = volunteerActivitiesDetailData.address
        tvAreaAddress.text = if (area == null) address else area + address
        val author = volunteerActivitiesDetailData.author
        tvAuthor.text = author
        val phone = volunteerActivitiesDetailData.phone
        tvPhone.text = phone
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun setAddActivityData(isSuccess: Boolean, msg: String, errorCode: Int) {}
    override fun setSignInData(isSuccess: Boolean, msg: String) {}
    override fun setVolunteerActivitiesSignInData(isSuccess: Boolean, msg: String, errorCode: Int) {}
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}