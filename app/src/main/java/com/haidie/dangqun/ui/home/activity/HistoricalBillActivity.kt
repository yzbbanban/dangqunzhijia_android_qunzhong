package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.DatePicker
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.BuildConfig
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.HistoricalBillContract
import com.haidie.dangqun.mvp.model.bean.HistoricalBillData
import com.haidie.dangqun.mvp.presenter.home.HistoricalBillPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.HistoricalBillAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.DatePickerDialog
import kotlinx.android.synthetic.main.activity_historical_bill.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/08/30 15:50
 * description  历史账单列表
 */
class HistoricalBillActivity : BaseActivity(), HistoricalBillContract.View {

    private val mPresenter by lazy { HistoricalBillPresenter() }
    private var  datePickerDialog: DatePickerDialog? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = Constants.PAGE
    private var size: Int = Constants.SIZE
    private var isRefresh = false
    private var year: Int? = null
    private var currentMonth: String? = null
    private var mData: List<HistoricalBillData.HistoricalBillItemData>? = null
    private var adapter: HistoricalBillAdapter? = null
    override fun getLayoutId(): Int = R.layout.activity_historical_bill

    override fun initData() {
        initDate()
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{onBackPressed()}
        tv_title.text = "历史账单"

        linear_layout.setOnClickListener {
            //弹出年月选择
            datePickerDialog!!.show()
        }
        smart_layout.setOnRefreshListener {
            isRefresh = true
            page = Constants.PAGE
            start()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener {
            isRefresh = false
            page++
            start()
            it.finishLoadMore(1000)
        }
        adapter = HistoricalBillAdapter(R.layout.historical_bill_item, mData)
        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            val intent = Intent(this@HistoricalBillActivity, BillDetailActivity::class.java)
            intent.putExtra(Constants.ID, adapter!!.data[position].id)
            startActivity(intent)
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
    private fun initDate() {
        val calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(this@HistoricalBillActivity, 0,smart_layout, object : DatePickerDialog.OnDateSetListener {
            @SuppressLint("SetTextI18n")
            override fun onDateSet(startDatePicker: DatePicker, startYear: Int, startMonthOfYear: Int, startDayOfMonth: Int) {
                tv_year_month.text = "$startYear-${(startMonthOfYear + 1)}"
                isRefresh = false
                isFirst = true
                page = 1
                year = startYear
                val month = startMonthOfYear + 1
                currentMonth = (month).toString()
                when (month) {
                    10,11,12 -> {}
                    else -> currentMonth = "0$month"
                }
                start()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val start = Calendar.getInstance()
        start.set(2018, 6, 1)
        //测试
        if (BuildConfig.DEBUG) {
            start.set(2017, 6, 7)
        }
        val minDate = start.timeInMillis
        datePickerDialog!!.setMinDate(minDate)
        val maxDate = calendar.timeInMillis
        datePickerDialog!!.setMaxDate(maxDate)
    }

    override fun start() { mPresenter.getHistoricalBillData(uid,year,currentMonth,page,size, token) }
    private var isFirst: Boolean = true
    override fun setHistoricalBillData(list: List<HistoricalBillData.HistoricalBillItemData>) {
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
                    if (page > Constants.PAGE) page --
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
    override fun showRefreshEvent() {  smart_layout.autoRefresh() }
}