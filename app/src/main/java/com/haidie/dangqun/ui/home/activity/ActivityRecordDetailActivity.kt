package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.ActivityRecordDetailContract
import com.haidie.dangqun.mvp.model.bean.ActivityRecordDetailData
import com.haidie.dangqun.mvp.presenter.home.ActivityRecordDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_record_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/29 14:37
 * description  活动记录详情
 */
class ActivityRecordDetailActivity : BaseActivity(),ActivityRecordDetailContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mId: Int? = null
    private var isRefresh = false
    private val mPresenter by lazy { ActivityRecordDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_record_detail

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "详情"
        mLayoutStatusView = multipleStatusView
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getActivityRecordDetailData(uid,token,mId!!)
    }

    @SuppressLint("SetTextI18n")
    override fun setActivityRecordDetailData(activityRecordDetailData: ActivityRecordDetailData) {
        val title = activityRecordDetailData.title
        tvTitle.text = title
        val url = Constants.HTML_BODY + activityRecordDetailData.content + Constants.BODY_HTML
        webView.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        val address = activityRecordDetailData.address
        tvAddress.text = address
        val start = activityRecordDetailData.start_time
        val end = activityRecordDetailData.end_time
        tvTime.text = "$start~$end"
        val author = activityRecordDetailData.author
        tvAuthor.text = author
        val phone = activityRecordDetailData.phone
        tvPhone.text = phone
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR ->   mLayoutStatusView?.showNoNetwork()
            else ->   mLayoutStatusView?.showError()
        }
    }

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