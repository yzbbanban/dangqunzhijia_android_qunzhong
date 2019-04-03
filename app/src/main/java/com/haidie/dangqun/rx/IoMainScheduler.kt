package com.haidie.dangqun.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin2
 *  on 2018/08/09  14:22
 * description
 */
class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(),AndroidSchedulers.mainThread())