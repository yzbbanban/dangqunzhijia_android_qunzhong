package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.LivingPaymentContract
import com.haidie.dangqun.mvp.model.bean.LivingPaymentData
import com.haidie.dangqun.mvp.presenter.home.LivingPaymentPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.LivingPaymentAdapter
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/08/29 11:09
 * description  应收账单列表
 */
class LivingPaymentActivity : BaseActivity(), LivingPaymentContract.View {

    private val mPresenter by lazy { LivingPaymentPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var size: Int = 10
    private var isRefresh = false
    private var id: Int = -1
    private var mData: List<LivingPaymentData.LivingPaymentItemData>? = null
    private var adapter: LivingPaymentAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {
        id = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "生活缴费"
//        tv_submit.visibility = View.VISIBLE
//        tv_submit.text = "历史账单"

        tv_submit.setOnClickListener {
            toActivity(HistoricalBillActivity::class.java)
        }
        smart_layout.setOnRefreshListener {
            isRefresh = true
            page = 1
            start()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener {
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
        adapter = LivingPaymentAdapter(R.layout.living_payment_item, mData)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            val intent = Intent(this@LivingPaymentActivity, BillDetailActivity::class.java)
            intent.putExtra(Constants.ID, adapter!!.data[position].id)
            startActivity(intent)
        }
        adapter!!.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener {
            _, view, position ->
            when (view.id) {
                R.id.tv_to_pay -> {
                    val intent = Intent(this@LivingPaymentActivity, BillDetailActivity::class.java)
                    intent.putExtra(Constants.ID, adapter!!.data[position].id)
                    startActivity(intent)
                }
            }
        }
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        smart_layout.setEnableHeaderTranslationContent(true)
        mLayoutStatusView = multiple_status_view
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getLivingPaymentData(uid, token, page, size, id)
    }

    private var isFirst: Boolean = true
    override fun setLivingPaymentData(list: List<LivingPaymentData.LivingPaymentItemData>) {
        mData = list
        when{
            isRefresh -> {
                if (mData!!.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smart_layout.isEnableLoadMore = false
                    smart_layout.isEnableRefresh = false
                    return
                }
                smart_layout.isEnableRefresh = true
                adapter!!.replaceData(mData!!)
                smart_layout.isEnableLoadMore = mData!!.size >= Constants.SIZE
                isFirst = false
            }
            else -> {
                if (mData!!.isNotEmpty()) {
                    adapter!!.addData(mData!!)
                    adapter!!.notifyDataSetChanged()
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
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
    override fun showRefreshEvent() {
        smart_layout.autoRefresh()
    }
}