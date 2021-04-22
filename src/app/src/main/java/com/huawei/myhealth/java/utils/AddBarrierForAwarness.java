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
package com.huawei.myhealth.java.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierUpdateRequest;
import com.huawei.myhealth.R;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class AddBarrierForAwarness
{
    public static void addBarrier(Context context, final String label, AwarenessBarrier barrier, PendingIntent pendingIntent) {
        BarrierUpdateRequest.Builder builder = new BarrierUpdateRequest.Builder();
        // When the status of the registered barrier changes, pendingIntent is triggered.
        // label is used to uniquely identify the barrier. You can query a barrier by label and delete it.
        BarrierUpdateRequest request = builder.addBarrier(label, barrier, pendingIntent).build();
        Awareness.getBarrierClient(context).updateBarriers(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, context.getString(R.string.add_barrier_success));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        showToast(context, e.toString());
                    }
                });
    }

    private static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
