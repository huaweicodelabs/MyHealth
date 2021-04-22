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
package com.huawei.myhealth.java.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentSigninBinding;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.UserProfile;
import com.huawei.myhealth.java.utils.CommonUtil;
import com.huawei.myhealth.java.utils.DefaultValues;

import java.util.List;
import java.util.Locale;
/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class SignInFragment extends Fragment implements View.OnClickListener {
    private String TAG = "SignInFragment";
    MyHealthDatabase db;
    FragmentSigninBinding binding;
    boolean mobileexist = false;



    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false);
        //db initialization
        db = MyHealthDatabase.getDatabase(getActivity());

        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }

        // set underline code for resend text
        binding.tvResendcode.setPaintFlags( binding.tvResendcode.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        // check for signIn
        isAlreadySignedIn();

        // signIn button action performed
        binding.signInBtn.setOnClickListener(this);

        // mobile edit text listner
        binding.enterMobileNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 10) {
                    changeBtnText(getResources().getString(R.string.sign_in));
                    binding.enterOTPTIL.setVisibility(View.GONE);
                    binding.tvResendcode.setVisibility(View.GONE);
                }
            }
        });

        // resend code action performed
        binding.tvResendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNoStr = binding.enterMobileNoET.getText().toString();
                applyAndVerifyMobileNo(mobileNoStr);
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onClick(View view) {
        String mobileNoStr = binding.enterMobileNoET.getText().toString();
        if (binding.signInBtn.getText().toString().equals(getResources().getString(R.string.sign_in))) {
            if ((!mobileNoStr.isEmpty() )&& mobileNoStr.length() == 10) {
                applyAndVerifyMobileNo(mobileNoStr);
            } else {
                Snackbar.make(binding.signInParentLay, getResources().getString(R.string.enter_validnumber), Snackbar.LENGTH_LONG).show();
            }
        } else {
            String otpStr = binding.enterOTPET.getText().toString();
            verifyOTP(mobileNoStr, otpStr);
        }
    }

    private void applyAndVerifyMobileNo(String mobileNoStr)
    {
        showHideProgressbar(true);
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder().action(1001).sendInterval(DefaultValues.VALUE_30).locale(Locale.ENGLISH).build();
        final Task task = PhoneAuthProvider.requestVerifyCode(getResources().getString(R.string.mobilecode), mobileNoStr, settings);
        task.addOnSuccessListener(new OnSuccessListener() {
                                      @Override
                                      public void onSuccess(Object o) {
                                          // onSuccess
                                          task.isSuccessful();
                                          showHideProgressbar(false);
                                          binding.enterOTPTIL.setVisibility( View.VISIBLE);
                                          binding.tvResendcode.setVisibility(View.VISIBLE);
                                          changeBtnText(getResources().getString(R.string.submit_OTP));
                                          Snackbar.make(binding.signInParentLay, getResources().getString(R.string.otp_sent), Snackbar.LENGTH_LONG).show();
                                      }
                                  }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // onFail
                Snackbar.make(binding.signInParentLay, e.toString(), Snackbar.LENGTH_LONG).show();
                showHideProgressbar(false);
                // binding.enterOTPTIL.visibility = View.GONE
                changeBtnText(getResources().getString(R.string.sign_In));
            }
        });
    }

    private void verifyOTP(String mobileNoStr,String otpStr)
    {
        AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(getResources().getString(R.string.mobilecode), mobileNoStr, null, otpStr);
        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener(new OnSuccessListener<SignInResult>() {
            @Override
            public void onSuccess(SignInResult signInResult) {
                // Obtain sign-in information.
                mobileexist = false;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // fetch Records
                        List<UserProfile> list_userprofile = db.userProfileDao().getUserData();
                        for(int i=0; i<list_userprofile.size(); i++) {
                            if(binding.enterMobileNoET.getText().toString().equals(list_userprofile.get(0).getMobileNoStr()))
                            {
                                mobileexist = true;
                            }
                        }
                    }
                });

                thread.start();
                thread.interrupt();

                if (mobileexist) {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
                } else {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.completeProfileFragmentDest);
                }
                // storing in shared preference
                SharedPreferences sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString(getResources().getString(R.string.mobilenum), binding.enterMobileNoET.getText().toString());
                editor.commit();
                binding.enterMobileNoET.setText("");
                binding.enterOTPET.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                CommonUtil.logger(e.toString(),TAG);
                if (e.getMessage().contains(getResources().getString(R.string.already_user))) {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
                } else {
                    isProfileCompleted();
                }
            }
        });
    }

    private void changeBtnText(String textOnBtn) {
        binding.signInBtn.setText(textOnBtn);
    }

    private void showHideProgressbar(boolean isVisibleCustom) {
        if(isVisibleCustom) {
            binding.signInProgressbar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.signInProgressbar.setVisibility(View.GONE);
        }
    }

    private void isAlreadySignedIn() {
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        if (user != null) {
            isProfileCompleted();
        }
    }

    private void isProfileCompleted() {
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        if (user.getDisplayName() == null) {
            //do nothing
        } else {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}