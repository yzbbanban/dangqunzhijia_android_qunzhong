package com.haidie.dangqun.mvp.model.bean

/**
 * Created by admin2
 *  on 2018/08/22  13:16
 * description
 */
data class RegisterData(val userinfo: UserInfo) {
    data class UserInfo(
            val id: Int,
            val group_id: Int,
            val username: String,
            val nickname: String,
            val mobile: String,
            val avatar: String,
            val token: String,
            val user_id: Int,
            val createtime: Int,
            val expiretime: Int,
            val expires_in: Int
    )
}