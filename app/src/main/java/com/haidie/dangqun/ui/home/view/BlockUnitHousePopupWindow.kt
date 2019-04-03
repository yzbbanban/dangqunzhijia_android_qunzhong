package com.haidie.dangqun.ui.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.BlockInfoData
import com.haidie.dangqun.mvp.model.bean.HouseListData
import com.haidie.dangqun.mvp.model.bean.UnitListData
import com.haidie.dangqun.ui.home.activity.OnlineHelpFormSubmissionActivity
import com.haidie.dangqun.utils.Preference
import razerdp.basepopup.BasePopupWindow

/**
 * Create by   Administrator
 *      on     2018/12/08 09:54
 * description
 */
class BlockUnitHousePopupWindow(onlineHelpFormSubmissionActivity:  OnlineHelpFormSubmissionActivity, viewGroup: ViewGroup)
    : BasePopupWindow(onlineHelpFormSubmissionActivity), View.OnClickListener  {
    private var popupView: View? = null
    private var view = viewGroup
    private var tvCancel: TextView? = null
    private var tvConfirm: TextView? = null
    private var tvBlock: WheelView? = null
    private var tvUnit: WheelView? = null
    private var tvHouse: WheelView? = null
    private var activity = onlineHelpFormSubmissionActivity
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    init {
        init()
        bindEvent()
    }
    override fun initShowAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0f)
        translateAnimation.duration = 500
        return translateAnimation
    }
    override fun initExitAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f)
        translateAnimation.duration = 500
        return translateAnimation
    }
    override fun onCreatePopupView(): View {
        popupView = LayoutInflater.from(context).inflate(R.layout.block_unit_house_popup_window, view,false)
        return popupView!!
    }
    override fun initAnimaView(): View = popupView!!.findViewById(R.id.popup_anim)
    override fun getClickToDismissView(): View = popupView!!.findViewById(R.id.click_to_dismiss)
    private fun init() {
        tvCancel = popupView!!.findViewById(R.id.tvCancel)
        tvConfirm = popupView!!.findViewById(R.id.tvConfirm)
        tvBlock = popupView!!.findViewById(R.id.tvBlock)
        tvUnit = popupView!!.findViewById(R.id.tvUnit)
        tvHouse = popupView!!.findViewById(R.id.tvHouse)
    }
    private fun bindEvent() {
        if (popupView != null) {
            popupView!!.findViewById<View>(R.id.tvCancel).setOnClickListener(this)
            popupView!!.findViewById<View>(R.id.tvConfirm).setOnClickListener(this)
        }
    }
    private lateinit var blockList: ArrayList<BlockInfoData>
    private lateinit var unitList : ArrayList<UnitListData>
    private lateinit var houseList: ArrayList<HouseListData>
    fun setBlockAdapter(blockInfoData: ArrayList<BlockInfoData>){
        blockList = blockInfoData
        val blocks = arrayListOf<String>()
        blockInfoData.forEach {
            blocks.add("${it.title}栋")
        }
        tvBlock?.let { it ->
            it.setCyclic(false)
            it.adapter = ArrayWheelAdapter(blocks)
            activity.getUnitListData(uid,token,blockInfoData[tvBlock!!.currentItem].title)
            it.setOnItemSelectedListener {
                activity.getUnitListData(uid,token,blockInfoData[it].title)
            }
        }
    }
    fun setUnitAdapter(unitListData: ArrayList<UnitListData>){
        unitList = unitListData
        val units = arrayListOf<String>()
        unitListData.forEach {
            units.add("${it.unit}单元")
        }
        tvUnit?.visibility = View.VISIBLE
        tvUnit?.let { it ->
            it.setCyclic(false)
            it.adapter = ArrayWheelAdapter(units)
            var unitItem = tvUnit!!.currentItem
            //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
            unitItem = if (unitItem >= units.size - 1) units.size - 1 else unitItem
            tvUnit?.currentItem = unitItem
            activity.getHouseListData(uid,token,blockList[tvBlock!!.currentItem].title,unitListData[tvUnit!!.currentItem].unit)
            it.setOnItemSelectedListener {
                activity.getHouseListData(uid,token,blockList[tvBlock!!.currentItem].title,unitListData[it].unit)
            }
        }
    }
    fun clearUnit(){
        tvUnit?.visibility = View.INVISIBLE
    }
    fun setHouseAdapter(houseListData: ArrayList<HouseListData>){
        houseList = houseListData
        val houses = arrayListOf<String>()
        houseListData.forEach {
            houses.add("${it.roomNo}室")
        }
        tvHouse?.visibility = View.VISIBLE
        tvHouse?.let { it ->
            it.setCyclic(false)
            it.adapter = ArrayWheelAdapter(houses)
            var houseItem = tvHouse!!.currentItem
            //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
            houseItem = if (houseItem >= houses.size - 1) houses.size - 1 else houseItem
            tvHouse?.currentItem = houseItem
        }
    }
    fun clearHouse(){
        tvHouse?.visibility = View.INVISIBLE
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvCancel -> dismiss()
            R.id.tvConfirm -> {
                //获取最后选择数据
                val block = blockList[tvBlock!!.currentItem].title
                val unit = unitList[tvUnit!!.currentItem].unit
                val house = houseList[tvHouse!!.currentItem].roomNo
                activity.getBlockUnitHouse("${block}栋${unit}单元$house")
                dismiss()
            }
        }
    }
}