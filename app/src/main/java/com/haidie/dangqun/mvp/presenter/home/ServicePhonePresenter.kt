package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.R
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ServicePhoneContract
import com.haidie.dangqun.mvp.model.bean.ServicePhoneData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by   Administrator
 *      on     2019/01/05 10:52
 * description
 */
class ServicePhonePresenter : BasePresenter<ServicePhoneContract.View>(),ServicePhoneContract.Presenter{
    private val titles = arrayOf( "嘉洋物业")
    override fun getServicePhoneData() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = Observable.create<ArrayList<ServicePhoneData>> {
            val data = ArrayList<ServicePhoneData>()
            for (title in titles) {
                val itemBean = ServicePhoneData()
                itemBean.title = title
                var avatars: IntArray? = null
                var nicknames: Array<String>? = null
                var groupNames: Array<String>? = null
                var positions: Array<String>? = null
                var mobiles: Array<String>? = null
                var ids: IntArray? = null
                when (title) {
                    titles[0] -> {
                        avatars = intArrayOf(
                                R.drawable.ic_header,
                                R.drawable.ic_header,
                                R.drawable.ic_header,
                                R.drawable.ic_header)
                        nicknames = arrayOf("周明亮","孙月振","陈新桥","王茂军")
                        groupNames = arrayOf("物业问题","物业问题","社区治安","水电维修")
                        positions = arrayOf("物业主管 保洁主管","内勤主管","保安主管","水电主管")
                        mobiles = arrayOf("15189054126","13776443621","15951596249","13815774850")
                        ids = intArrayOf(0,1,2,3)
                    }
                }
                if (avatars != null) {
                    val itemBeanList = ArrayList<ServicePhoneData.GridItemBean>()
                    for (j in avatars.indices) {
                        itemBeanList.add(ServicePhoneData.GridItemBean(avatars[j],
                                nicknames!![j],groupNames!![j],positions!![j],
                                mobiles!![j],ids!![j]))
                    }
                    itemBean.list = itemBeanList
                }
                data.add(itemBean)
            }
            it.onNext(data)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mRootView?.apply {
                        dismissLoading()
                        setServicePhoneData(it)
                    }
                }, {
                    mRootView?.dismissLoading()
                })
        addSubscription(disposable)
    }
}