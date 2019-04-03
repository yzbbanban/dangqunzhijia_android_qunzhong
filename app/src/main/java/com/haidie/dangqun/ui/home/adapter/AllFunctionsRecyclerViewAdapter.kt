package com.haidie.dangqun.ui.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.AllFunctionsDataBean
import com.haidie.dangqun.ui.home.activity.*
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

/**
 * Created by admin2
 *  on 2018/08/18  15:59
 * description
 */
class AllFunctionsRecyclerViewAdapter(activity: Activity,layoutResId: Int, data: ArrayList<AllFunctionsDataBean>?)
    : BaseQuickAdapter<AllFunctionsDataBean, BaseViewHolder>(layoutResId, data) {
    private var textTypeFaceMedium: Typeface? = null
    private val names = arrayOf(
            "生活公告","家政服务","生活缴费","服务电话","障碍报修","问题上报",
            "活动公告","活动记录","四点半课堂","周五合唱团","社会活动记录","成绩查询",
            "三务公开","政务政策","办事指南","在线咨询","医院挂号",
            "志愿活动","活动记录","积分兑换",
            "活动公告","活动记录")
    private var ids = intArrayOf(
            0,1,2,3,4,5,
            6,7,8,9,10,11,
            12,13,14,15,16,
            17,18,19,
            20,21)
    private var mActivity: Activity

    init {
        textTypeFaceMedium = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_MEDIUM)
        mActivity = activity
    }
    override fun convert(helper: BaseViewHolder?, item: AllFunctionsDataBean?) {
        val textView = helper!!.getView<TextView>(R.id.tv_title)
        textView.text = item!!.title
        textView.typeface = textTypeFaceMedium
        val gridItemBean = item.gridItemBean
        val recyclerView = helper.getView<RecyclerView>(R.id.gv_recycler_item)
        recyclerView.layoutManager = GridLayoutManager(mContext, 4)
        val gridViewAdapter = AllFunctionsGridViewAdapter(R.layout.all_functions_grid_view_item, gridItemBean)
        recyclerView.adapter = gridViewAdapter
        helper.setNestView(R.id.ll_recycler_item_group)
        helper.addOnClickListener(R.id.gv_recycler_item)
        gridViewAdapter.setOnItemClickListener {
            _, _, position ->
            val name = gridItemBean!![position].desc
            val id = gridItemBean[position].id
            when (id) {
                ids[0] -> {
                    //跳转到生活公告页面
                    val intent = Intent(mActivity, LifeBulletinActivity::class.java)
                    intent.putExtra(Constants.ID,"29")
                    mActivity.startActivity(intent)
                    mActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //跳转到家政服务页面
                ids[1] ->   toActivity(mActivity,HousekeepingActivity::class.java)
                    //跳转到生活缴费页面
                ids[2] ->  toActivity(mActivity, LifePaymentActivity::class.java)
                //服务电话
                ids[3] ->  {
//                    val intent = Intent(mActivity, LifeBulletinDetailActivity::class.java)
//                    intent.putExtra(Constants.ID,"19")
//                    intent.putExtra(Constants.TYPE,Constants.ARTICLE)
//                    mActivity.startActivity(intent)
//                    mActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                    toActivity(mActivity, ServicePhoneActivity::class.java)
                }
                //跳转到障碍报修页面
                ids[4] ->  toActivity(mActivity,FaultRepairActivity::class.java)
                ids[5] ->  AndPermission.with(mActivity)
                        .runtime()
                        .permission(Permission.ACCESS_FINE_LOCATION,Permission.READ_EXTERNAL_STORAGE)
                        .rationale(RuntimeRationale())
                        //跳转到问题上报页面
                        .onGranted { toActivity(mActivity,ProblemReportActivity::class.java) }
                        .onDenied { permissions ->  showSettingDialog(mActivity, permissions)  }
                        .start()
                //跳转到活动公告页面
                ids[6] ->  toActivity(mActivity,CaringActivitiesActivity::class.java)
                //跳转到活动记录页面
                ids[7],ids[18] -> {
                    //活动类型0,活动登记,1社会活动登记,2智慧互动活动记录
                    var mType = -1
                    when (gridItemBean[position].id) {
                        ids[7] -> mType = 0
                        ids[18] -> mType = 2
                    }
                    if (mType != -1) {
                        val intent = Intent(mActivity, ActivityRecordActivity::class.java)
                        intent.putExtra(Constants.TYPE, mType)
                        intent.putExtra(Constants.TEXT, name)
                        mActivity.startActivity(intent)
                        mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                    }
                }
//                四点半课堂
                ids[8],ids[9] -> {
                    val type = when(name){
                        names[8] -> 0
                        names[9] -> 1
                        else -> -1
                    }
                    val intent = Intent(mActivity, HalfPastFourClassActivity::class.java)
                    intent.putExtra(Constants.TYPE,type)
                    intent.putExtra(Constants.TEXT,name)
                    mActivity.startActivity(intent)
                    mActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
//                社会活动记录
                ids[10] -> showShort("敬请期待")
                //跳转到成绩查询页面
                ids[11]->  toActivity(mActivity,ScoreQueryActivity::class.java)
                //跳转到三务公开列表页面
                ids[12]->  toActivity(mActivity,ThreeAffairsListActivity::class.java)
                ids[13],ids[14]->  {
//                    政务政策","办事指南
                    var type : Int? = null
                    var idLife = ""
                    when(name){
                        names[13] -> {
                            idLife ="67"
                            type = 2
                        }
                        names[14] -> {
                            idLife ="70"
                            type = 2
                        }
                    }
                    val intent = Intent(mActivity, GovernmentArticleListActivity::class.java)
                    intent.putExtra(Constants.ID,idLife)
                    intent.putExtra(Constants.TEXT,name)
                    intent.putExtra(Constants.TYPE,type)
                    mActivity.startActivity(intent)
                    mActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                //跳转到在线咨询页面
                ids[15] ->  toActivity(mActivity,OnlineConsultingActivity::class.java)
                //医院挂号  跳转到医疗健康列表页面
                ids[16] ->  {
//                    toActivity(mActivity,MedicalHealthActivity::class.java)
                    showShort("敬请期待")
                }
                //志愿活动 //跳转到活动公告页面
                ids[17] ->  toActivity(mActivity, ActivityBulletinActivity::class.java)
                //跳转到积分商城列表页面
                ids[19] ->  toActivity(mActivity,PointsMallListActivity::class.java)
                //跳转到智慧新风-活动公告页面
                ids[20] ->  EventAnnouncementsWebViewActivity.start(mActivity,Constants.EVENT_ANNOUNCEMENTS)
                //跳转到智慧新风-活动记录页面
                ids[21] ->  EventAnnouncementsWebViewActivity.start(mActivity,Constants.ACTIVITY_RECORD)
            }
        }
    }
    private fun toActivity(activity: Activity, clazz : Class<*>){
        activity.startActivity(Intent(activity, clazz))
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
    }
    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }
    private fun setPermission() {
        AndPermission.with(mActivity)
                .runtime()
                .setting()
                .start()
    }
    private var myToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showShort( message: String){
        if (myToast == null) {
            myToast = Toast.makeText(mActivity, message, Toast.LENGTH_SHORT)
        } else {
            myToast!!.setText(message)
            myToast!!.duration = Toast.LENGTH_SHORT
        }
        myToast!!.show()
    }
}