package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.LifePaymentContract
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData
import com.haidie.dangqun.mvp.model.bean.LifePaymentData
import com.haidie.dangqun.mvp.presenter.home.LifePaymentPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.LifePaymentAdapter
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_life_payment.*

/**
 * Create by   Administrator
 *      on     2018/11/26 18:03
 * description  生活缴费
 */
class LifePaymentActivity : BaseActivity(),LifePaymentContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { LifePaymentPresenter() }
    private lateinit var mData: MutableList<LifePaymentData>
    private val titles = arrayOf( "物业费","电费","燃气费")
    private var isRefresh = false
    private var id: Int = -1
    private var pvOptions: OptionsPickerView<String>? = null
    override fun getLayoutId(): Int = R.layout.activity_life_payment

    override fun initData() {
        mData = mutableListOf()
        mData.add(LifePaymentData(titles[0],R.drawable.living_payment,R.color.property_fee_color))
        mData.add(LifePaymentData(titles[1],R.drawable.furniture_decoration,R.color.electricity_fee_color))
        mData.add(LifePaymentData(titles[2],R.drawable.finance,R.color.electricity_fee_color))
    }

    private var isShow: Boolean = false

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView
        ivBack.setOnClickListener { onBackPressed() }

        tvPaymentAccount.setOnClickListener {
//            跳转到缴费账户页面
            toActivity(PaymentAccountActivity::class.java)
        }
        val adapter = LifePaymentAdapter(R.layout.life_payment_item, mData)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        tvNewPayment.setOnClickListener {
//            跳转到新增缴费账户页面
            toActivity(NewPaymentAccountActivity::class.java)
        }
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            when (mData[position].title) {
                titles[0] -> {
//                    跳转到账单列表
//                    toActivity(PropertyPaymentActivity::class.java)
                    if (id == -1) {
                        showShort("请选择楼栋单元房间号")
                        return@OnItemClickListener
                    }
                    val intent = Intent(this, LivingPaymentActivity::class.java)
                    intent.putExtra(Constants.ID,id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                }
            }
        }
        rlSpaceUnitHouse.setOnClickListener {
            getData()
            isShow = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getBoundPaymentAccountListData(uid, token)
    }
    private fun getData(){
        mPresenter.getBoundPaymentAccountListData(uid, token)
    }
    override fun showRefreshEvent() {
        isRefresh = true
        getData()
    }
    private var isFirst = true
    @SuppressLint("SetTextI18n")
    override fun setBoundPaymentAccountListData(boundPaymentAccountListData: ArrayList<BoundPaymentAccountListData>?) {
        if (isFirst || isRefresh) {
            val i = boundPaymentAccountListData!!.size - 1
            tvSpaceUnitHouse.text = "新城名苑${boundPaymentAccountListData[i].space}栋${boundPaymentAccountListData[i].unit}单元${boundPaymentAccountListData[i].house}"
            id = boundPaymentAccountListData[i].id
            isFirst = false
        }
        pvOptions = OptionsPickerBuilder(this) {
            options1, _, _, _ ->
            tvSpaceUnitHouse.text = "新城名苑${boundPaymentAccountListData!![options1].space}栋${boundPaymentAccountListData[options1].unit}单元${boundPaymentAccountListData[options1].house}"
            id = boundPaymentAccountListData[options1].id
        }.build()
        val showList = mutableListOf<String>()
        boundPaymentAccountListData?.forEach {
            showList.add("新城名苑${it.space}栋${it.unit}单元${it.house}")
        }
        pvOptions?.setPicker(showList)
        if (isShow) {
            pvOptions?.show()
            isShow = false
        }

    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NO_INFO -> {
                tvSpaceUnitHouse.text = ""
                isShow = false
            }
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}