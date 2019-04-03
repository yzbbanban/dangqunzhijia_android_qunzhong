package com.haidie.dangqun.mvp.model.bean

import java.util.*

/**
 * Create by   Administrator
 *      on     2018/09/14 16:50
 * description
 */
data class ProvinceCityAreaData(var province: List<JsonBean>?, var city: List<List<String>>?, var area: List<List<List<String>>>?) {
    data class JsonBean(var name: String, var city: ArrayList<CityBean>){
        data class CityBean(var name: String, var area: Array<String>?) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as CityBean

                if (name != other.name) return false
                if (!Arrays.equals(area, other.area)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + (area?.let { Arrays.hashCode(it) } ?: 0)
                return result
            }
        }
    }
}