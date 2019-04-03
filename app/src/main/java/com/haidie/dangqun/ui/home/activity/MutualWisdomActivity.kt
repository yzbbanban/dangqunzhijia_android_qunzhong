package com.haidie.dangqun.ui.home.activity

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
 *      on     2018/09/11 09:00
 * description  智慧互助
 */
class MutualWisdomActivity : BaseActivity() {
    private var dataList: MutableList<Map<String, Any>>? = null
    //图标
    val icon = intArrayOf(R.drawable.online_help,R.drawable.appraisal, R.drawable.voluntary_organization,
            R.drawable.voluntary_integral,R.drawable.points_mall)
    //图标下的文字
    private val names = arrayOf("网上求助","评比评选", "志愿组织",
            "志愿活动", "积分商城")
    private val ids = arrayOf("-6","-5","-3","-2","-1")
    override fun getLayoutId(): Int = R.layout.activity_property_housekeeping

    override fun initData() {
        dataList = mutableListOf()
        for (i in icon.indices) {
            val map = HashMap<String, Any>()
            map[Constants.ICON] = icon[i]
            map[Constants.TEXT] = names[i]
            map[Constants.ID] = ids[i]
            dataList!!.add(map)
        }
    }

    override fun initView() {
        iv_back.visibility = View.VISIBLE
        tv_title.text = "智慧互助"
        iv_back.setOnClickListener { onBackPressed() }

        val adapter = PropertyHousekeepingAdapter(R.layout.property_housekeeping_item, dataList)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            val name = dataList!![position][Constants.TEXT] as String
            when (name) {
                    //跳转到积分商城列表页面
                names[names.size - 1] ->  toActivity(PointsMallListActivity::class.java)
                    //跳转到志愿者活动列表页面
                names[names.size - 2] ->  toActivity(VolunteerActivitiesListActivity::class.java)
                    //跳转到志愿组织页面
                names[names.size - 3] ->  toActivity(VoluntaryOrganizationActivity::class.java)
                    //跳转到评比评选列表页面
                names[names.size - 4] ->  toActivity(EvaluationListActivity::class.java)
                    //跳转到网上求助列表页面
                else ->   toActivity(OnlineHelpListActivity::class.java)
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
        smart_layout.setOnRefreshListener({ refreshLayout ->
            initData()
            refreshLayout.finishRefresh(1000)
        })
        smart_layout.isEnableLoadMore = false
    }
    override fun start() {}
}