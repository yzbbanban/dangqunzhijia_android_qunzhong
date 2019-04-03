package com.haidie.dangqun.mvp.model.bean

/**
 * Created by admin2
 *  on 2018/08/10  14:56
 * description
 */
data class MineData(val id: Int,
                    val nickname: String,
                    val mobile: String,
                    val score: Int,
                    val avatar: String,
                    val username: String,
                    val level: Int,
                    val createtime: Int,
                    val gender: String,
                    val birthday: String?,
                    val count: Count){
    data class Count(
            val collection: Int,
            val attention: Int,
            val fans: Int)
}