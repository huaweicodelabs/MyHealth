package com.huawei.myhealth.utils

import android.util.Log
import android.widget.TextView

class CommonUtil
{
    /*
     * Send the operation result logs to the logcat
     *
     * @param string indicating the log string
     * @param  tag activity log tag
     */
    public companion object {
    fun logger(string: String, tag: String?) {
        Log.i(tag, string)
    }
    }
}