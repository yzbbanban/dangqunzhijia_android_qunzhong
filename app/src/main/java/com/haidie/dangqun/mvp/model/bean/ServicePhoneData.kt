package com.haidie.dangqun.mvp.model.bean

/**
 * Created by admin2
 *  on 2018/08/18  15:35
 * description
 */
data class ServicePhoneData(var list : ArrayList<GridItemBean>? = null, var title : String? = null) {

    data class GridItemBean( var avatar: Int,
                             var nickname: String,val groupName: String,val position: String,
                             var mobile: String, var id: Int)
}