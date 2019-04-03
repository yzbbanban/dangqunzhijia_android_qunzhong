package com.haidie.dangqun.mvp.presenter.mine

import android.content.Context
import com.google.gson.Gson
import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.ReleaseActivitiesContract
import com.haidie.dangqun.mvp.model.bean.ProvinceCityAreaData
import com.haidie.dangqun.mvp.model.mine.ReleaseActivitiesModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray

/**
 * Create by   Administrator
 *      on     2018/09/14 16:22
 * description
 */
class ReleaseActivitiesPresenter : BasePresenter<ReleaseActivitiesContract.View>(),ReleaseActivitiesContract.Presenter{

    private val releaseActivitiesModel by lazy { ReleaseActivitiesModel() }
    private var options1Items: List<ProvinceCityAreaData.JsonBean> = mutableListOf()
    private val options2Items = mutableListOf<List<String>>()
    private val options3Items = mutableListOf<List<List<String>>>()
    override fun getJson(context: Context) {
        addSubscription(Observable.create<ProvinceCityAreaData>{
            val jsonData = CommonUtils.getJson(context, "province.json")
            val jsonBean = parseData(jsonData)
            /**
             * 添加省份数据
             * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
             * PickerView会通过getPickerViewText方法获取字符串显示出来。
             */
            options1Items = jsonBean
            for (i in jsonBean.indices) {//遍历省份
                val cityList = ArrayList<String>()//该省的城市列表（第二级）
                val provinceAreaList = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）
                for (c in 0 until jsonBean[i].city.size) {//遍历该省份的所有城市
                    val cityName = jsonBean[i].city[c].name
                    cityList.add(cityName)//添加城市
                    val cityAreaList = arrayListOf<String>()//该城市的所有地区列表
                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (jsonBean[i].city[c].area == null || jsonBean[i].city[c].area!!.isEmpty()) {
                        cityAreaList.add("")
                    } else {  cityAreaList.addAll(jsonBean[i].city[c].area!!) }
                    provinceAreaList.add(cityAreaList)//添加该省所有地区数据
                }
                /**
                 * 添加城市数据
                 */
                options2Items.add(cityList)
                /**
                 * 添加地区数据
                 */
                options3Items.add(provinceAreaList)
            }
            val provinceCityAreaData = ProvinceCityAreaData(null,null,null)
            provinceCityAreaData.province = options1Items
            provinceCityAreaData.city = options2Items
            provinceCityAreaData.area = options3Items
            it.onNext(provinceCityAreaData)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mRootView!!.setJson(it) }))
    }
    private fun parseData(result: String): ArrayList<ProvinceCityAreaData.JsonBean> {
        val detail = ArrayList<ProvinceCityAreaData.JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(data.optJSONObject(i).toString(), ProvinceCityAreaData.JsonBean::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) { e.printStackTrace() }
        return detail
    }
    override fun getReleaseActivitiesData(uid: RequestBody, type: RequestBody, title: RequestBody, content: RequestBody, start_time: RequestBody,
                                          end_time: RequestBody, area: RequestBody, address: RequestBody, token: RequestBody, part: MultipartBody.Part) {
        checkViewAttached()
        val disposable = releaseActivitiesModel.getReleaseActivitiesData(uid, type, title, content, start_time, end_time, area, address, token, part)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("发布失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setReleaseActivitiesData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            if (e.errorCode == ApiErrorCode.SUCCESS){
                                setReleaseActivitiesData(false,e.mMessage)
                            }else{
                                showError(e.mMessage,e.errorCode)
                            }
                        }
                    }
                })
        addSubscription(disposable)
    }

}