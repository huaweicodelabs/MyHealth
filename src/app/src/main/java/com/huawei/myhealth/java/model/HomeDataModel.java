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
package com.huawei.myhealth.java.model;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class HomeDataModel {
  private String homeCardTitleTV;
  private String homeDataCountTV;
  private int homeCardIConsIMV;

    public HomeDataModel(String homeCardTitleTV, String homeDataCountTV, int homeCardIConsIMV) {
        this.homeCardTitleTV = homeCardTitleTV;
        this.homeDataCountTV = homeDataCountTV;
        this.homeCardIConsIMV = homeCardIConsIMV;
    }

    public String getHomeCardTitleTV() {
        return homeCardTitleTV;
    }

    public void setHomeCardTitleTV(String homeCardTitleTV) {
        this.homeCardTitleTV = homeCardTitleTV;
    }

    public String getHomeDataCountTV() {
        return homeDataCountTV;
    }

    public void setHomeDataCountTV(String homeDataCountTV) {
        this.homeDataCountTV = homeDataCountTV;
    }

    public int getHomeCardIConsIMV() {
        return homeCardIConsIMV;
    }

    public void setHomeCardIConsIMV(int homeCardIConsIMV) {
        this.homeCardIConsIMV = homeCardIConsIMV;
    }
}
