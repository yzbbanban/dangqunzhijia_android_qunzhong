package com.haidie.dangqun.ui.mine.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.ExchangeDataListContract
import com.haidie.dangqun.mvp.model.bean.ExchangeDataListData
import com.haidie.dangqun.mvp.presenter.mine.ExchangeDataListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.mine.adapter.ExchangeDataListAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/13 15:50
 * description  我的兑换数据列表
 */
class ExchangeDataListActivity : BaseActivity(),ExchangeDataListContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false

    private val mPresenter by lazy { ExchangeDataListPresenter() }
    private lateinit var mData: MutableList<ExchangeDataListData.ListBean>
    private lateinit var mAdapter: ExchangeDataListAdapter
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "我的兑换"
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multiple_status_view
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener{
            page = 1
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener{
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
    }
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = ExchangeDataListAdapter(R.layout.layout_exchange_data_list_item, mData)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun start() { mPresenter.getMyExchangeDataListData(uid,page,Constants.SIZE,token) }
    private var isFirst: Boolean = true
    override fun setMyExchangeDataListData(exchangeDataListData: ExchangeDataListData) {
        mData = exchangeDataListData.list
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
    override fun dismissLoading() {  mLayoutStatusView?.showContent() }
}