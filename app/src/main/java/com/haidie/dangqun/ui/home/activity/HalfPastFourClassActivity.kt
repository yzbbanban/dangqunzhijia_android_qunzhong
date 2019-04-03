package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.HalfPastFourClassContract
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassData
import com.haidie.dangqun.mvp.presenter.home.HalfPastFourClassPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.HalfPastFourClassAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/11 13:25
 * description  四点半课堂
 */
class HalfPastFourClassActivity : BaseActivity(),HalfPastFourClassContract.View {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var type: Int = -1     //课堂记录类型0,表示四点半课堂,1周五合唱团
    private var isRefresh = false
    private var mTitle: String? = null
    private val mPresenter by lazy { HalfPastFourClassPresenter() }
    private lateinit var mData: MutableList<HalfPastFourClassData.HalfPastFourClassItemData>
    private lateinit var mAdapter: HalfPastFourClassAdapter
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view

    override fun initData() {
        type = intent.getIntExtra(Constants.TYPE,-1)
        mTitle = intent.getStringExtra(Constants.TEXT)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = mTitle
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
        mData = mutableListOf()
        mAdapter = HalfPastFourClassAdapter(R.layout.half_past_four_class_item,mData)
        mAdapter.setFridayChoir(type)
        mAdapter.setOnItemClickListener{ _, _, position ->
            val intent = Intent(this, HalfPastFourClassDetailActivity::class.java)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            intent.putExtra(Constants.TYPE,type)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun start() {
        mPresenter.getHalfPastFourClassData(uid,token,page,Constants.SIZE,type)
    }
    private var isFirst: Boolean = true
    override fun setHalfPastFourClassData(halfPastFourClassData: HalfPastFourClassData) {
        mData = halfPastFourClassData.list
        when{
            isRefresh -> {
                isFirst = false
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
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
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