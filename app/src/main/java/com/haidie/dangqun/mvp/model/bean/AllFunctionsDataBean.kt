package com.haidie.dangqun.mvp.model.bean

/**
 * Created by admin2
 *  on 2018/08/18  15:35
 * description
 */
data class AllFunctionsDataBean(var gridItemBean : ArrayList<GridItemBean>? = null,var title : String? = null) {

    data class GridItemBean(var desc: String, var imageUrl: Int, var id: Int)
}