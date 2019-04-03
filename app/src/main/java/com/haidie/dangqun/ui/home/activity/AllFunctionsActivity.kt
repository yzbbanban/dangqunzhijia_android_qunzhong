package com.haidie.dangqun.ui.home.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.AllFunctionsContract
import com.haidie.dangqun.mvp.model.bean.AllFunctionsDataBean
import com.haidie.dangqun.mvp.presenter.home.AllFunctionsPresenter
import com.haidie.dangqun.ui.home.adapter.AllFunctionsRecyclerViewAdapter
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_all_functions.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by admin2
 *  on 2018/08/18  14:55
 * description  全部功能
 */
class AllFunctionsActivity : BaseActivity(), AllFunctionsContract.View {

    private val mPresenter by lazy { AllFunctionsPresenter() }
    private var allFunctionsRecyclerViewAdapter: AllFunctionsRecyclerViewAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_all_functions

    override fun initData() {}

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{
            onBackPressed()
        }
        tv_title.text = "全部功能"
        recycler_view_all_functions.setHasFixedSize(true)
        recycler_view_all_functions.layoutManager = LinearLayoutManager(this)
        recycler_view_all_functions.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        mLayoutStatusView = multipleStatusView
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        //初始化数据
        mPresenter.initFunctionsData()
    }
    private fun refreshAdapter(data: ArrayList<AllFunctionsDataBean>) {
        if (allFunctionsRecyclerViewAdapter == null) {
            allFunctionsRecyclerViewAdapter = AllFunctionsRecyclerViewAdapter(this,
                    R.layout.all_functions_recycler_view_item, data)
            recycler_view_all_functions.adapter = allFunctionsRecyclerViewAdapter
        }else{
            allFunctionsRecyclerViewAdapter?.replaceData(data)
        }

    }
    override fun setFunctionsData(functionsDataList: ArrayList<AllFunctionsDataBean>) {
        //刷新适配器
        refreshAdapter(functionsDataList)
        LogHelper.d("================"+Gson().toJson(functionsDataList))
    }
    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}