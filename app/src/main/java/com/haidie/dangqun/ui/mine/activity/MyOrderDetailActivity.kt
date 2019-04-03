package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.MyOrderDetailContract
import com.haidie.dangqun.mvp.model.bean.MyOrderDetailData
import com.haidie.dangqun.mvp.presenter.mine.MyOrderDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.OrderCompletedActivity
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_my_order_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/14 14:10
 * description  家政(物业)服务订单详情页面
 */
class MyOrderDetailActivity : BaseActivity(),MyOrderDetailContract.View {

    private var mId: Int? = null
    private var title: String? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var isRefresh = false
    private val mPresenter by lazy { MyOrderDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_my_order_detail
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
        title = intent.getStringExtra(Constants.TEXT)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {  onBackPressed()  }
        tv_title.text = title
        mLayoutStatusView = multiple_status_view
        tv_evaluation.setOnClickListener {
            val intent = Intent(this@MyOrderDetailActivity, OrderCompletedActivity::class.java)
            intent.putExtra(Constants.ID, mId)
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getMyOrderDetailData(uid,mId!!,token) }
    override fun setMyOrderDetailData(myOrderDetailData: MyOrderDetailData) {
        tv_title_content.text = myOrderDetailData.title
        tv_status.text = myOrderDetailData.status
        tv_time.text = myOrderDetailData.time
        tv_type.text = myOrderDetailData.type
        tv_orderNo.text = myOrderDetailData.orderNo
        tv_category.text = myOrderDetailData.category
        tv_content.text = myOrderDetailData.content
        tv_create_time.text = myOrderDetailData.create_time
        tv_bus_name.text = myOrderDetailData.bus_name
        tv_name.text = myOrderDetailData.name
        tv_phone.text = myOrderDetailData.phone
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