package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerListContract
import com.haidie.dangqun.mvp.model.bean.VolunteerListData
import com.haidie.dangqun.mvp.presenter.home.VolunteerListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.VolunteerListAdapter
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_volunteer_list.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/11 16:46
 * description  志愿者列表
 */
class VolunteerListActivity : BaseActivity(),VolunteerListContract.View {

    private val mPresenter by lazy { VolunteerListPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private var mId: Int? = null
    private var mType: Int? = null
    private var searchContent = Constants.EMPTY_STRING
    private var type: String? = null
    private lateinit var mData: MutableList<VolunteerListData.VolunteerListItemData>
    private lateinit var mAdapter: VolunteerListAdapter
    override fun getLayoutId(): Int = R.layout.activity_volunteer_list
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
        mType = intent.getIntExtra(Constants.TYPE,Constants.NEGATIVE_ONE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "志愿者名单"
        if (2 == mType) {
            linear_layout.visibility = View.VISIBLE
            type = Constants.VOLUNTEER
        } else if (0 == mType) {
            type = Constants.PENDING_VOLUNTEER
        }

        setRefresh()
        initRecyclerView()

        mLayoutStatusView = multiple_status_view

        tv_search.setOnClickListener {
            searchContent = et_edit_content.text.toString()
            if (searchContent.isEmpty()) {
                showShort("请输入志愿者名称")
                return@setOnClickListener
            }
            closeKeyboard(et_edit_content,this)
            page = 1
            isRefresh = true
            start()
        }
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener {
            page = 1
            isRefresh = true
            searchContent = Constants.EMPTY_STRING
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
        mAdapter = VolunteerListAdapter(R.layout.layout_volunteer_list_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            val intent = Intent(this@VolunteerListActivity, VolunteerDetailActivity::class.java)
            intent.putExtra(Constants.ID, mAdapter.data[position].id)
            intent.putExtra(Constants.TYPE, type)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() { mPresenter.getVolunteerListData(uid,mId!!,mType!!,searchContent,page,Constants.SIZE,token) }
    private var isFirst: Boolean = true
    override fun setVolunteerListData(volunteerListData: VolunteerListData) {
        mData = volunteerListData.list
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
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
    override fun showRefreshEvent() { smart_layout.autoRefresh() }
}