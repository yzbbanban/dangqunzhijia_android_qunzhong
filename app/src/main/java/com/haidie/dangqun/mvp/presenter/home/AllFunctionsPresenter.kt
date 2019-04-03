package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.R
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.AllFunctionsContract
import com.haidie.dangqun.mvp.model.bean.AllFunctionsDataBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin2
 *  on 2018/08/21  17:10
 * description
 */
class AllFunctionsPresenter : BasePresenter<AllFunctionsContract.View>(), AllFunctionsContract.Presenter{
    private val titles = arrayOf( "智慧物业","智慧关爱","智慧政务","智慧互动","智慧新风")
    override fun initFunctionsData() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = Observable.create<ArrayList<AllFunctionsDataBean>> {
            val data = ArrayList<AllFunctionsDataBean>()
            for (title in titles) {
                val itemBean = AllFunctionsDataBean()
                itemBean.title = title
                var imageUrl: IntArray? = null
                var desc: Array<String>? = null
                var ids: IntArray? = null
                when (title) {
                    titles[0] -> {
                        imageUrl = intArrayOf(
                                R.drawable.property_announcement,
                                R.drawable.property_housekeeping,
                                R.drawable.icon_living_payment,
                                R.drawable.icon_service_phone,
                                R.drawable.icon_fault_repair,
                                R.drawable.icon_problem_report)
                        desc = arrayOf("生活公告","家政服务","生活缴费","服务电话","障碍报修","问题上报")
                        ids = intArrayOf(0,1,2,3,4,5)
                    }
                    titles[1] -> {
                        imageUrl = intArrayOf(
                                R.drawable.activity_bulletin,
                                R.drawable.icon_activity_record,
                                R.drawable.icon_half_past_four_class,
                                R.drawable.icon_friday_choir,
                                R.drawable.icon_social_activity_record,
                                R.drawable.icon_score_query)
                        desc = arrayOf("活动公告","活动记录","四点半课堂","周五合唱团","社会活动记录","成绩查询")
                        ids = intArrayOf(6,7,8,9,10,11)
                    }
                    titles[2] -> {
                        imageUrl = intArrayOf(
                                R.drawable.three_public_affairs,
                                R.drawable.icon_government_policy,
                                R.drawable.icon_handing_guideline,
                                R.drawable.online_consulting,
                                R.drawable.medical_health)
                        desc = arrayOf("三务公开","政务政策","办事指南","在线咨询","医院挂号")
                        ids = intArrayOf(12,13,14,15,16)
                    }
                    titles[3] -> {
                        imageUrl = intArrayOf(
                                R.drawable.icon_voluntary_activities,
                                R.drawable.icon_activity_record,
                                R.drawable.icon_exchange)
                        desc = arrayOf("志愿活动","活动记录","积分兑换")
                        ids = intArrayOf(17,18,19)
                    }
                    titles[4] -> {
                        imageUrl = intArrayOf(R.drawable.activity_bulletin,R.drawable.icon_activity_record)
                        desc = arrayOf("活动公告","活动记录")
                        ids = intArrayOf(20,21)
                    }
                }
                if (imageUrl != null) {
                    val itemBeanList = ArrayList<AllFunctionsDataBean.GridItemBean>()
                    for (j in imageUrl.indices) {
                        itemBeanList.add(AllFunctionsDataBean.GridItemBean(desc!![j], imageUrl[j],ids!![j]))
                    }
                    itemBean.gridItemBean = itemBeanList
                }
                data.add(itemBean)
            }
            it.onNext(data)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mRootView?.apply {
                        dismissLoading()
                        setFunctionsData(it)
                    }
                }, {
                    mRootView?.dismissLoading()
                })
        addSubscription(disposable)
    }
}