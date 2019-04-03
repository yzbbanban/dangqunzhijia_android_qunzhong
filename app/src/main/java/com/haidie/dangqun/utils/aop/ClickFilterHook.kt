package com.haidie.dangqun.utils.aop

import com.haidie.dangqun.utils.LogHelper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * Create by   Administrator
 *      on     2018/11/05 14:51
 * description
 */
@Aspect
class ClickFilterHook {
    private var sLastClick = 0L
    private val filterTime = 500L

    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    fun onClickBehavior() {
    }
    /**
     * 找到处理的切点
     * * *(..)  可以处理所有的方法
     */
    @Pointcut("execution(@com.haidie.dangqun.utils.aop.CheckOnClick * *(..))")
    fun checkOnClickBehavior() {
    }

    @Around("onClickBehavior() || checkOnClickBehavior()")
    fun clickFilterHook(joinPoint: ProceedingJoinPoint) {
        if (System.currentTimeMillis() - sLastClick >= filterTime) {
            sLastClick = System.currentTimeMillis()
            try {
                joinPoint.proceed()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        } else {
            LogHelper.d("=======\n避免重复点击")
        }
    }
}