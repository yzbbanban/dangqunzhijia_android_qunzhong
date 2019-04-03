package com.haidie.dangqun.ui.mine.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.haidie.dangqun.R
import com.haidie.dangqun.utils.LogHelper
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils
import java.io.File

/**
 * Created by admin2
 *  on 2018/08/18  11:45
 * description
 */
class GridImageAdapter() : RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {
    val TYPE_CAMERA = 1
    val TYPE_PICTURE = 2
    private var list: MutableList<LocalMedia> = mutableListOf()
    private var selectMax = 9
    private var context: Context? = null
    private var mInflater: LayoutInflater ?= null
    /**
     * 点击添加图片跳转
     */
    private var mOnAddPicClickListener: OnAddPicClickListener? = null

    interface OnAddPicClickListener {
        fun onAddPicClick()
    }
    constructor(context: Context, mOnAddPicClickListener: OnAddPicClickListener) : this() {
        this.context = context
        mInflater = LayoutInflater.from(context)
        this.mOnAddPicClickListener = mOnAddPicClickListener
    }

    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    fun setList(list: MutableList<LocalMedia>) {
        this.list = list
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var mImg: ImageView = view.findViewById(R.id.fiv)
        internal var llDel: LinearLayout = view.findViewById(R.id.ll_del)
        internal var tv_duration: TextView = view.findViewById(R.id.tv_duration)
    }
    override fun getItemCount(): Int {
        return if (list.size < selectMax) {
            list.size + 1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowAddItem(position)) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater!!.inflate(R.layout.gv_filter_image, parent, false)
        return ViewHolder(view)
    }

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (list.size == 0) 0 else list.size
        return position == size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.add_pic_1x)
            viewHolder.mImg.setOnClickListener({ mOnAddPicClickListener!!.onAddPicClick() })
            viewHolder.llDel.visibility = View.INVISIBLE
        } else {
            viewHolder.llDel.visibility = View.VISIBLE
            viewHolder.llDel.setOnClickListener({
                val index = viewHolder.adapterPosition
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION) {
                    list.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, list.size)
                }
            })
            val media = list[position]
            val mimeType = media.mimeType
            val path: String
            path = if (media.isCut && !media.isCompressed) {
                // 裁剪过
                media.cutPath
            } else if (media.isCompressed || media.isCut && media.isCompressed) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                media.compressPath
            } else {
                // 原图
                media.path
            }
            // 图片
            if (media.isCompressed) {
                LogHelper.d("compress image result:" + File(media.compressPath).length() / 1024 + "k")
                LogHelper.d("压缩地址::" + media.compressPath)

            }

            LogHelper.d("原图地址::" + media.path)
            val pictureType = PictureMimeType.isPictureType(media.pictureType)
            if (media.isCut) {
                LogHelper.d("裁剪地址::" + media.cutPath)
            }
            val duration = media.duration
            viewHolder.tv_duration.visibility = if (pictureType == PictureConfig.TYPE_VIDEO)
                View.VISIBLE
            else
                View.GONE
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.tv_duration.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context!!, R.drawable.picture_audio)
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable!!, 0)
            } else {
                val drawable = ContextCompat.getDrawable(context!!, R.drawable.video_icon)
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable!!, 0)
            }
            viewHolder.tv_duration.text = DateUtils.timeParse(duration)
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.audio_placeholder)
            } else {
                val options = RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.color_f6)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(viewHolder.itemView.context)
                        .load(path)
                        .apply(options)
                        .into(viewHolder.mImg)
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener({ v ->
                    val adapterPosition = viewHolder.adapterPosition
                    mItemClickListener!!.onItemClick(adapterPosition, v)
                })
            }
        }
    }

    var mItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View)
    }
}