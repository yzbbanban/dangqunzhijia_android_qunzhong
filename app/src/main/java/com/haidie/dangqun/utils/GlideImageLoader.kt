package com.haidie.dangqun.utils

import android.content.Context
import android.widget.ImageView

/**
 * Created by admin2
 *  on 2018/08/11  17:16
 * description
 */
class GlideImageLoader : com.youth.banner.loader.ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        ImageLoader.load(context!!, path, imageView!!)
    }
}