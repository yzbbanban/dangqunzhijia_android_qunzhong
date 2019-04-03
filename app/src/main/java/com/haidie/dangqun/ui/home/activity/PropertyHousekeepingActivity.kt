package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.adapter.PropertyHousekeepingAdapter
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_property_housekeeping.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/07 16:10
 * description  物业家政
 */
class PropertyHousekeepingActivity : BaseActivity() {
    private var dataList: MutableList<Map<String, Any>>? = null
    //图标
    val icon = intArrayOf(R.drawable.life_bulletin, R.drawable.housekeeping, R.drawable.property_service,
//            R.drawable.icon_property_fee,
            R.drawable.problem_report
//            ,R.drawable.living_payment
    )
    //图标下的文字
    val name = arrayOf("生活公告", "家政服务", "物业服务",
//            "工单管理",
            "故障报修"
//            ,"生活缴费"
    )
    private val ids = arrayOf("29", "31", "32",
//            "-1",
            "-2"
//            ,"-3"
    )
    override fun getLayoutId(): Int = R.layout.activity_property_housekeeping
    override fun initData() { initPropertyHousekeeping() }
    private fun initPropertyHousekeeping() {
        dataList = ArrayList()
        for (i in icon.indices) {
            val map = HashMap<String, Any>()
            map[Constants.ICON] = icon[i]
            map[Constants.TEXT] = name[i]
            map[Constants.ID] = ids[i]
            dataList!!.add(map)
        }
    }
    override fun initView() {
        iv_back.visibility = View.VISIBLE
        tv_title.text = "家政物业"
        iv_back.setOnClickListener { onBackPressed() }

        val adapter = PropertyHousekeepingAdapter(R.layout.property_housekeeping_item, dataList)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            val id = dataList!![position][Constants.ID] as String
            when (id) {
                ids[0] -> {
                    //跳转到生活公告页面
                    val intent = Intent(this, LifeBulletinActivity::class.java)
                    intent.putExtra(Constants.ID,ids[0])
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                    //跳转到工单管理页面
//                ids[ids.size-3] ->  toActivity(WorkOrderManagementActivity::class.java)
                ids[ids.size-1] -> {
                    //跳转到故障报修页面
                    toActivity(FaultRepairActivity::class.java)
//                    //跳转到提交工单页面
//                    val intent = Intent(this, SubmitWorkOrderActivity::class.java)
//                    intent.putExtra(Constants.ID,"32 ")
//                    startActivity(intent)
                }
                    //跳转到生活缴费页面
//                ids[ids.size-1] ->  toActivity(LivingPaymentActivity::class.java)
                else -> {
                    //跳转到服务分类列表页面
                    val intent = Intent(this, ServiceCategoryActivity::class.java)
                    intent.putExtra(Constants.TEXT,dataList!![position][Constants.TEXT] as String)
                    intent.putExtra(Constants.ID,id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        smart_layout.setEnableHeaderTranslationContent(true)

        setRefresh()
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener {
            initPropertyHousekeeping()
            it.finishRefresh(1000)
        }
        smart_layout.isEnableLoadMore = false
    }
    override fun start() {}
}