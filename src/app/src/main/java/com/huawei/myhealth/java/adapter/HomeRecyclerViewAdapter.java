package com.huawei.myhealth.java.adapter;
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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.myhealth.R;
import com.huawei.myhealth.java.model.HomeDataModel;
import com.huawei.myhealth.java.ui.fragments.HomeScreenFragment;

import java.util.ArrayList;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    ArrayList homeDataModelList = new ArrayList();
    HomeScreenFragment homeScreenFragment;


    public HomeRecyclerViewAdapter(ArrayList homeDataModelList, HomeScreenFragment homeScreenFragment) {
        this.homeDataModelList = homeDataModelList;
        this.homeScreenFragment = homeScreenFragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_home_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItems((HomeDataModel) homeDataModelList.get(position));
        holder.homeCardParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //action to navigate food screen
                if (position == 0) {
                    NavHostFragment.findNavController(homeScreenFragment).navigate(R.id.action_homeScreenDest_to_timePeriodDialog);
                }
                //action to navigate water intake
                if (position == 1) {
                    NavHostFragment.findNavController(homeScreenFragment).navigate(R.id.waterIntakeFragment);
                }
                //action to navigate calBurn screen
                else if (position == 3) {
                    NavHostFragment.findNavController(homeScreenFragment).navigate(R.id.calBurnedFragment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeDataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout homeCardParentLay;
        TextView homeCardTitleTV, homeDataCountTV;
        ImageView homeCardIConsIMV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItems(HomeDataModel homeDataModel) {

            homeCardParentLay = itemView.findViewById(R.id.homeCardParentLay);
            homeCardTitleTV = itemView.findViewById(R.id.homeCardTitleTV);
            homeDataCountTV = itemView.findViewById(R.id.homeDataCountTV);
            homeCardIConsIMV = itemView.findViewById(R.id.homeCardIConsIMV);
            homeCardTitleTV.setText(homeDataModel.getHomeCardTitleTV());
            homeDataCountTV.setText(homeDataModel.getHomeDataCountTV());
            homeCardIConsIMV.setImageResource(homeDataModel.getHomeCardIConsIMV());
        }


    }

}
