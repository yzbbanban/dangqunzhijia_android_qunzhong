package com.haidie.dangqun.ui.home.activity

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.PaymentAccountContract
import com.haidie.dangqun.mvp.event.ReloadLifePaymentEvent
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData
import com.haidie.dangqun.mvp.presenter.home.PaymentAccountPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.home.adapter.PaymentAccountAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/27 16:11
 * description  缴费账户
 */
class PaymentAccountActivity : BaseActivity(),PaymentAccountContract.View {

    private val mPresenter by lazy { PaymentAccountPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var isRefresh = false
    private lateinit var mData: ArrayList<BoundPaymentAccountListData>
    private lateinit var mAdapter: PaymentAccountAdapter
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "缴费账户"
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multiple_status_view
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }
    private fun initRecyclerView() {
        mData = arrayListOf()
        mAdapter = PaymentAccountAdapter(R.layout.payment_account_item, mData)
        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener {
            _, view, position ->
            when (view.id) {
                R.id.tvUnbind -> AlertDialog.Builder(this)
                            .setTitle("是否确认解绑")
                            .setPositiveButton("是") {
                                _, _ ->
                                mPresenter.getUnbindPaymentAccountResultData(uid,token,mAdapter.data[position].id)
                            }
                            .setNegativeButton("否",null)
                            .create()
                            .show()
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getBoundPaymentAccountListData(uid, token)
    }
    private var isFirst: Boolean = true
    override fun setBoundPaymentAccountListData(boundPaymentAccountListData: ArrayList<BoundPaymentAccountListData>?) {
        mData = boundPaymentAccountListData!!
        when{
            isRefresh -> {
                if (mData.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smart_layout.isEnableLoadMore = false
                    smart_layout.isEnableRefresh = false
                    return
                }
                smart_layout.isEnableRefresh = true
                mAdapter.replaceData(mData)
                smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                isFirst = false
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                    isFirst = false
                } else {
                    if (isFirst) {
                        mLayoutStatusView?.showEmpty()
                        smart_layout.isEnableRefresh = false
                        smart_layout.isEnableLoadMore = false
                        showShort( "暂无数据内容")
                    }else{
                        showShort(resources.getString(R.string.load_more_no_data))
                    }
                }
            }
        }
    }
    override fun setUnbindPaymentAccountResultData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("解绑成功")
//            刷新页面数据
            smart_layout.autoRefresh()
            RxBus.getDefault().post(ReloadLifePaymentEvent())
        }else{
            showShort(if (msg.isEmpty()) "解绑失败" else msg)
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