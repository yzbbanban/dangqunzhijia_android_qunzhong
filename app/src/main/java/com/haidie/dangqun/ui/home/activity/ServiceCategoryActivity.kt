package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.ServiceCategoryContract
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.mvp.presenter.home.ServiceCategoryPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.ServiceCategoryAdapter
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_service_category.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/10 10:37
 * description  服务分类列表
 */
class ServiceCategoryActivity : BaseActivity(), ServiceCategoryContract.View {
    private val mPresenter by lazy { ServiceCategoryPresenter() }

    private var mId: String? = null
    private var title: String? = null
    private var isRefresh = false
    private var page: Int = 1
    private lateinit var mAdapter: ServiceCategoryAdapter
    private lateinit var mData: MutableList<ServiceCategoryData.ServiceCategoryItemData>
    override fun getLayoutId(): Int = R.layout.activity_service_category
    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        mId = intent.getStringExtra(Constants.ID)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = title
        mLayoutStatusView = multiple_status_view
        mData = ArrayList()
        mAdapter = ServiceCategoryAdapter(R.layout.service_category_item,mData)
        mAdapter.setOnItemClickListener{
            _, _, position ->
            val intent = Intent(this@ServiceCategoryActivity, ServiceListActivity::class.java)
            intent.putExtra(Constants.ID,"${mAdapter.data[position].id}")
            intent.putExtra(Constants.TEXT,mAdapter.data[position].name)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
        smart_layout.setOnRefreshListener {
            isRefresh = true
            page = 1
            start()
            it.finishRefresh(1000)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getServiceCategoryData(mId!!) }
    private var isFirst: Boolean = true
    override fun setServiceCategoryData(serviceCategoryData: ServiceCategoryData) {
        mData = serviceCategoryData.list
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
}