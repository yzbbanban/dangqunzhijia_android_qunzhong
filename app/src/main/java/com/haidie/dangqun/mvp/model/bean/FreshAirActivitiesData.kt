package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/12/10 14:09
 * description
 */
data class FreshAirActivitiesData(
        val list: MutableList<FreshAirActivitiesItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class FreshAirActivitiesItemData(
            val id: Int,
            val author_id: Int,
            val is_official: Int,
            val title: String,
            val content: String,
            val cover_sum: Int,
            val cover_one: String,
            val cover_two: String,
            val cover_three: String,
            val is_top_news: Int,
            val is_top: Int,
            val top_loop: Int,
            val create_time: String,
            val update_time: String,
            val status: Int,
            val delete_tag: Int,
            val toutiao_article_type: String?,
            val read_sum: Int,
            val zan_sum: Int,
            val unlike_sum: Int,
            val is_vote: Int,
            val vote_type: String?,
            val comment_sum: Int,
            val start_time: String?,
            val end_time: String?,
            val address: String?,
            val manager: String?,
            val contact: String?,
            val vote_end_time: String?,
            val is_register_copy: Int,
            val module_type: Int,
            val first_type: Int,
            val second_type: String?,
            val third_type: String?,
            val activity_status: String?,
            val num: String?,
            val need_sum: String?,
            val remark: String?,
            val points: String?,
            val is_limit: String?,
            val video1: String?,
            val video2: String?,
            val video3: String?,
            val nickname: String,
            val avatar: String,
            val toutiao_article_type_name: String?,
            val first_type_name: String,
            val img_sum: Int)
}