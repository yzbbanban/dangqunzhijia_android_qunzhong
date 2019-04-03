package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/12/10 15:00
 * description
 */
data class FreshAirActivitiesDetailData(
        val id: Int,
        val author_id: Int,
        val is_official: Int,
        val title: String,
        val content: String,
        val cover_sum: Int,
        val cover_one: String,
        val cover_two: String?,
        val cover_three: String?,
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
        val vote_type: Int,
        val img1: String?,
        val img2: String?,
        val img3: String?,
        val img4: String?,
        val img5: String?,
        val img6: String?,
        val img7: String?,
        val img8: String?,
        val img9: String?,
        val comment_sum: Int,
        val start_time: String,
        val end_time: String,
        val address: String,
        val manager: String,
        val contact: String,
        val vote_end_time: String,
        val is_register_copy: Int,
        val module_type: Int,
        val first_type: Int,
        val second_type: Int?,
        val third_type: Int?,
        val activity_status: Int?,
        val num: Int?,
        val need_sum: Int?,
        val remark: String?,
        val points: String?,
        val is_limit: String?,
        val video1: String?,
        val video2: String?,
        val video3: String?,
        val nickname: String,
        val avatar: String,
        val activity_name: String,
        val options: List<Option>,
        val voteStatus: Int,
        val register_arr: List<String>?) {
    data class Option(
            val id: Int,
            val option: String,
            val image: String,
            val create_time: String,
            val update_time: String,
            val delete_tag: Int,
            val relate_id: Int,
            val num: Int?,
            val module_type: Int
    )
}