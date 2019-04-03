package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.WorkOrderManagementContract
import com.haidie.dangqun.mvp.model.bean.OrderListData
import com.haidie.dangqun.mvp.presenter.home.WorkOrderManagementPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.WorkOrderManagementAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_work_order_management.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/08 09:56
 * description  工单管理
 */
class WorkOrderManagementActivity : BaseActivity(), WorkOrderManagementContract.View {

    private val mPresenter by lazy { WorkOrderManagementPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private val id = 2
    private var isRefresh = false
    private  var mData: MutableList<OrderListData.OrderListItemData>? = null
    private var isFirst: Boolean = true
    private  var mOrderListData: OrderListData? = null
    override fun getLayoutId(): Int = R.layout.activity_work_order_management
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        tv_title.text = "工单管理"
        iv_back.setOnClickListener { onBackPressed() }
        mLayoutStatusView = multiple_status_view
        setRefresh()
        initRecyclerView()
    }
    private lateinit var mAdapter: WorkOrderManagementAdapter
    private fun setRefresh() {
        smart_layout.setOnRefreshListener({ refreshLayout ->
            page = 1
            isRefresh = true
            start()
            refreshLayout.finishRefresh(1000)
        })
        smart_layout.setOnLoadMoreListener({ refreshLayout ->
            page++
            isRefresh = false
            start()
            refreshLayout.finishLoadMore(1000)
        })
        smart_layout.isEnableAutoLoadMore = true
    }
    override fun showRefreshEvent() { smart_layout.autoRefresh() }
    private fun initRecyclerView() {
        mData = mutableListOf()
        mAdapter = WorkOrderManagementAdapter(R.layout.layout_work_order_management_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            val intent = Intent(this@WorkOrderManagementActivity, WorkOrderManagementDetailActivity::class.java)
            intent.putExtra(Constants.ID, mAdapter.data[position].id)
            intent.putExtra(Constants.GROUP_ID, mOrderListData!!.group_id)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
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
    override fun start() { mPresenter.getOrderListData(uid,page,Constants.SIZE,id, token) }
    override fun setOrderListData(orderListData: OrderListData) {
        mOrderListData = orderListData
        mData = orderListData.list
        when{
            isRefresh -> {
                if (mData!!.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smart_layout.isEnableLoadMore = false
                    smart_layout.isEnableRefresh = false
                    return
                }
                mAdapter.setGroupId(orderListData.group_id)
                smart_layout.isEnableRefresh = true
                mAdapter.replaceData(mData!!)
                smart_layout.isEnableLoadMore = mData!!.size >= Constants.SIZE
                isFirst = false
            }
            else -> {
                if (mData!!.isNotEmpty()) {
                    mAdapter.addData(mData!!)
                    mAdapter.setGroupId(orderListData.group_id)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData!!.size >= Constants.SIZE
                    isFirst = false
                } else {
                    if (page > 1) page --
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
}