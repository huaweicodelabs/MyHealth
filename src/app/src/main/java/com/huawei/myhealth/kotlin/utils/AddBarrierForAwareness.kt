/*
 *
 *  * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.huawei.myhealth.utils

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.huawei.hms.kit.awareness.Awareness
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier
import com.huawei.hms.kit.awareness.barrier.BarrierUpdateRequest
import com.huawei.myhealth.R

/**
 * @since 2020
 * @author Huawei DTSE India
 */

object AddBarrierForAwareness {
    fun addBarrier(context: Context, label: String?, barrier: AwarenessBarrier?, pendingIntent: PendingIntent?) {
        val builder = BarrierUpdateRequest.Builder()
        // When the status of the registered barrier changes, pendingIntent is triggered.
        // label is used to uniquely identify the barrier. You can query a barrier by label and delete it.
        val request = builder.addBarrier(label!!, barrier!!, pendingIntent!!).build()
        Awareness.getBarrierClient(context).updateBarriers(request)
                .addOnSuccessListener { showToast(context, context.getString(R.string.add_barrier_success)) }
                .addOnFailureListener { e ->
                    showToast(context, e.toString())
                }
    }
    private fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}