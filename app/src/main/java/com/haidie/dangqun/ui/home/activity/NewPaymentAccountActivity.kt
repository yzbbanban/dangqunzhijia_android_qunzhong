package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.NewPaymentAccountContract
import com.haidie.dangqun.mvp.event.ReloadLifePaymentEvent
import com.haidie.dangqun.mvp.model.bean.*
import com.haidie.dangqun.mvp.presenter.home.NewPaymentAccountPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_new_payment_account.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/26 19:32
 * description  新增缴费账户
 */
class NewPaymentAccountActivity : BaseActivity(),NewPaymentAccountContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { NewPaymentAccountPresenter() }
    private var isRefresh = false
    private var houseId: Int = -1
    private var pvOptions: OptionsPickerView<String>? = null
    private var pvOptionsUnit: OptionsPickerView<String>? = null
    private var pvOptionsHouse: OptionsPickerView<String>? = null
    override fun getLayoutId(): Int = R.layout.activity_new_payment_account

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{
            onBackPressed()
        }
        tv_title.text = "新增缴费账户"
        tv_submit.text = "宿迁"
        tv_submit.visibility = View.VISIBLE

    }

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView
        tvSpace.setOnClickListener {
            //调用获取楼栋列表
            mPresenter.getBlockInfoData(uid, token)
        }
        tvUnit.setOnClickListener {
            val space = tvSpace.text.toString()
            if (space.isEmpty()) {
                showShort("请选择楼栋")
                return@setOnClickListener
            }
            //调用获取单元的列表
            mPresenter.getUnitListData(uid,token,space.replace("栋",""))
        }
        tvHouse.setOnClickListener {
            val space = tvSpace.text.toString()
            if (space.isEmpty()) {
                showShort("请选择楼栋")
                return@setOnClickListener
            }
            val unit = tvUnit.text.toString()
            if (unit.isEmpty()) {
                showShort("请选择单元")
                return@setOnClickListener
            }
            //调用获取房子的列表
            mPresenter.getHouseListData(uid,token,space.replace("栋",""),
                    unit.replace("单元",""))
        }
        tvNextStep.setOnClickListener {
            val space = tvSpace.text.toString()
            if (space.isEmpty()) {
                showShort("请选择楼栋")
                return@setOnClickListener
            }
            val unit = tvUnit.text.toString()
            if (unit.isEmpty()) {
                showShort("请选择单元")
                return@setOnClickListener
            }
            val house = tvHouse.text.toString()
            if (house.isEmpty()) {
                showShort("请选择房间编号")
                return@setOnClickListener
            }
            val username = tvUsername.text.toString()
            if (username.isEmpty()) {
                showShort("所选房间编号暂无业主信息")
                return@setOnClickListener
            }
//            val phone = tvPhone.text.toString()
//            if (phone.isEmpty()) {
//                showShort("所选房间编号暂无业主信息")
//                return@setOnClickListener
//            }
            if (!cb.isChecked) {
                showShort("未同意协议")
                return@setOnClickListener
            }
            mPresenter.getAddPaymentAccountResultData(uid,token,houseId)
        }
    }

    override fun setBlockInfoData(blockInfoData: ArrayList<BlockInfoData>?, msg: String) {
        if (blockInfoData == null){
            showShort(msg)
            return
        }
        val showList = mutableListOf<String>()
        blockInfoData.forEach {
            showList.add("${it.title}栋")
        }
        pvOptions = OptionsPickerBuilder(this) {
            options1, _, _, _ ->
            tvSpace.text = showList[options1]
            tvUnit.text = ""
            tvHouse.text = ""
            tvUsername.text = ""
            tvPhone.text = ""
        }.build()
        pvOptions?.setPicker(showList)
        if (!pvOptions!!.isShowing){
            pvOptions?.show()
        }
    }
    override fun setUnitListData(unitListData: ArrayList<UnitListData>?, msg: String) {
        if (unitListData == null || unitListData.isEmpty()){
            showShort(if (msg.isEmpty()) "数据为空" else msg)
            return
        }
        val showListUnit = mutableListOf<String>()
        unitListData.forEach {
            showListUnit.add("${it.unit}单元")
        }
        pvOptionsUnit = OptionsPickerBuilder(this) {
            options1, _, _, _ ->
            tvUnit.text = showListUnit[options1]
            tvHouse.text = ""
            tvUsername.text = ""
            tvPhone.text = ""
        }.build()
        pvOptionsUnit?.setPicker(showListUnit)
        if (!pvOptionsUnit!!.isShowing){
            pvOptionsUnit?.show()
        }
    }
    override fun setHouseListData(houseListData: ArrayList<HouseListData>?, msg: String) {
        if (houseListData == null || houseListData.isEmpty()){
            showShort(if (msg.isEmpty()) "数据为空" else msg)
            return
        }
        val showListHouse = mutableListOf<String>()
        houseListData.forEach {
            showListHouse.add(it.roomNo)
        }
        pvOptionsHouse = OptionsPickerBuilder(this) {
            options1, _, _, _ ->
            tvHouse.text = showListHouse[options1]
//            调用获取业主姓名,手机号信息接口
            houseId = houseListData[options1].id
            mPresenter.getRoomNumberUserInfoData(uid,token,houseId.toString())
        }.build()
        pvOptionsHouse?.setPicker(showListHouse)
        if (!pvOptionsHouse!!.isShowing){
            pvOptionsHouse?.show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun setBuildingUnitAndRoomNumberData(buildingUnitAndRoomNumberData: BuildingUnitAndRoomNumberData) {}
    override fun setRoomNumberUserInfoData(roomNumberUserInfoData: RoomNumberUserInfoData?, msg: String) {
        if (roomNumberUserInfoData == null) {
            showShort(msg)
        }else{
//            涉及到隐私处理
            var username = roomNumberUserInfoData.username
            if (username.length == 2) {
                username = "${username.substring(0,1)}*"
            }else if (username.length > 2){
                val first = username.first()
                val last = username.last()
                var join = ""
                for (i in 0 until username.length -2){
                    join += "*"
                }
                username = "$first$join$last"
            }
            tvUsername.text = username
            var phone = roomNumberUserInfoData.phone
            val first = phone.substring(0,3)
            val last = phone.substring(7,phone.length)
            phone = "$first****$last"
            tvPhone.text = phone
        }
    }
    override fun setAddPaymentAccountResultData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
//          返回到之前页面刷新生活缴费
            showShort("新增成功")
            RxBus.getDefault().post(ReloadLifePaymentEvent())
            finish()
        }else{
            showShort(if (msg.isEmpty()) "新增失败" else msg )
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
}