package com.haidie.dangqun.ui.home.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.ServicePhoneContract
import com.haidie.dangqun.mvp.model.bean.ServicePhoneData
import com.haidie.dangqun.mvp.presenter.home.ServicePhonePresenter
import com.haidie.dangqun.ui.home.adapter.ServicePhoneRecyclerViewAdapter
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_service_phone.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2019/01/05 10:36
 * description  服务电话
 */
class ServicePhoneActivity : BaseActivity(),ServicePhoneContract.View {

    private var isRefresh = false
    private val mPresenter by lazy { ServicePhonePresenter() }
    private  var adapter: ServicePhoneRecyclerViewAdapter? = null
    override fun getLayoutId(): Int = R.layout.activity_service_phone

    override fun initData() {}

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "服务电话"
        setRefresh()
        mLayoutStatusView = multipleStatusView
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        }
    }
    private fun setRefresh() {
        smartLayout.setOnRefreshListener{
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getServicePhoneData()
    }
    override fun setServicePhoneData(servicePhoneData: ArrayList<ServicePhoneData>) {
        if (adapter == null) {
            adapter = ServicePhoneRecyclerViewAdapter(R.layout.service_phone_recycler_view_item, servicePhoneData)
            recyclerView.adapter = adapter
        } else {
            adapter?.replaceData(servicePhoneData)
        }
    }
    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}