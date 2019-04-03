package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.HalfPastFourClassDetailContract
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassDetailData
import com.haidie.dangqun.mvp.presenter.home.HalfPastFourClassDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_half_past_four_class_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/11 14:25
 * description  四点半课堂-详情
 */
class HalfPastFourClassDetailActivity : BaseActivity(),HalfPastFourClassDetailContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mId: Int? = null
    private var isRefresh = false
    private var type: Int = -1     //课堂记录类型0,表示四点半课堂,1周五合唱团
    private val mPresenter by lazy { HalfPastFourClassDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_half_past_four_class_detail

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
        type = intent.getIntExtra(Constants.TYPE,-1)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "详情"
        mLayoutStatusView = multipleStatusView
    }

    override fun initView() {
        mPresenter.attachView(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getHalfPastFourClassDetailData(uid,token,mId!!)
    }
    override fun setHalfPastFourClassDetailData(halfPastFourClassDetailData: HalfPastFourClassDetailData) {
        val pic = halfPastFourClassDetailData.pic
        ImageLoader.load(this,pic,ivPic)
        var title = ""
        when (type) {
            0 ->  title = halfPastFourClassDetailData.cid
            1 ->  title = halfPastFourClassDetailData.content
        }
        tvTitle.text = title
        tvTime.text = halfPastFourClassDetailData.start_time
        tvUsername.text = halfPastFourClassDetailData.username
        tvPhone.text = halfPastFourClassDetailData.phone
        var text = ""
        halfPastFourClassDetailData.sign_list?.forEach {
            text += "${it.name},"
        }
        if (text.isNotEmpty()) {
            tvSignList.text = text.substring(0,text.length-1)
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
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}