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

package com.huawei.myhealth.ui.fragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.PhoneAuthProvider
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.huawei.myhealth.R
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.databinding.FragmentSigninBinding
import com.huawei.myhealth.utils.CommonUtil
import com.huawei.myhealth.utils.DefaultValues
import java.util.*
/**
 * @since 2020
 * @author Huawei DTSE India
 */

class SignInFragment : Fragment(), View.OnClickListener {
    private val TAG = "SignInFragment"
    var db: MyHealthDatabase? = null
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        val view = binding.root

        // db initilize
        db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }

        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // set underline code for resend text
        binding.tvResendcode.setPaintFlags(binding.tvResendcode.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        // check for signIn
        isAlreadySignedIn()

        // signIn button action performed
        binding.signInBtn.setOnClickListener(this)

        // mobile edit text listner
        binding.enterMobileNoET.doAfterTextChanged {
            if (binding.enterMobileNoET.text?.length!! < 10) {
                changeBtnText(resources.getString(R.string.sign_in))
                binding.enterOTPTIL.visibility = View.GONE
                binding.tvResendcode.visibility = View.GONE
            }
        }

        // resend code action performed
        binding.tvResendcode!!.setOnClickListener {
            val mobileNoStr: String = binding.enterMobileNoET.text.toString()
            applyAndVerifyMobileNo(mobileNoStr)
        }
        return view
    }
    private fun applyAndVerifyMobileNo(mobileNoStr: String) {

            showHideProgressbar(true)
            val settings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                    .sendInterval(DefaultValues.VALUE_30)
                    .locale(Locale.ENGLISH)
                    .build()
            val task = PhoneAuthProvider.requestVerifyCode(resources.getString(R.string.mobilecode), mobileNoStr, settings)
            task.addOnSuccessListener {
                // onSuccess
                task.isSuccessful
                showHideProgressbar(false)
                binding.enterOTPTIL.visibility = View.VISIBLE
                binding.tvResendcode.visibility = View.VISIBLE
                changeBtnText(resources.getString(R.string.submit_OTP))
                Snackbar.make(binding.signInParentLay, resources.getString(R.string.otp_sent), Snackbar.LENGTH_LONG).show()
            }.addOnFailureListener { e ->
                // onFail
                Snackbar.make(binding.signInParentLay, e.toString(), Snackbar.LENGTH_LONG).show()
                showHideProgressbar(false)
                // binding.enterOTPTIL.visibility = View.GONE
                changeBtnText(resources.getString(R.string.sign_In))
            }

    }

    private fun verifyOTP(mobileNoStr: String, otpStr: String) {
        val credential = PhoneAuthProvider.credentialWithVerifyCode(resources.getString(R.string.mobilecode), mobileNoStr, null, otpStr)

        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener {
                    // Obtain sign-in information.
                    var mobileexist = false
                    //fetch Records
                    val thread = Thread {
                        db?.userProfileDao()?.getUserData()?.forEach()
                        {
                            if (binding.enterMobileNoET.text.toString().equals(it.mobileNoStr)) {
                                mobileexist = true
                            }
                        }
                    }
                    thread.start()
                    thread.interrupt()
                    if (mobileexist) {
                        findNavController().navigate(R.id.homeScreenDest)
                    } else {
                        findNavController().navigate(R.id.completeProfileFragmentDest)
                    }
                    // storing in shared preference
                    val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
                    var editor = sharedPreference?.edit()
                    editor?.putString(resources.getString(R.string.mobilenum), binding.enterMobileNoET.text.toString())
                    editor?.commit()
                    binding.enterMobileNoET.setText("")
                    binding.enterOTPET.setText("")
                }
                .addOnFailureListener { e ->
                    CommonUtil.logger(e.toString(),TAG)
                    if (e.message?.contains(resources.getString(R.string.already_user))!!) {
                        findNavController().navigate(R.id.homeScreenDest)
                    } else {
                        isProfileCompleted()
                    }
                }
    }

    private fun changeBtnText(textOnBtn: String) {
        binding.signInBtn.text = textOnBtn
    }

    private fun showHideProgressbar(isVisibleCustom: Boolean) {
        binding.signInProgressbar.isVisible = isVisibleCustom
    }

    private fun isAlreadySignedIn() {
        val user = AGConnectAuth.getInstance().currentUser
        if (user != null) {
            isProfileCompleted()
        }
    }

    private fun isProfileCompleted() {
        val user = AGConnectAuth.getInstance().currentUser
        if (user.displayName == null) {
            //do nothing
        } else {
            findNavController().navigate(R.id.homeScreenDest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onClick(v: View?) {
        val mobileNoStr: String = binding.enterMobileNoET.text.toString()
        if (binding.signInBtn.text.toString() == resources.getString(R.string.sign_in)) {
            if (mobileNoStr.isNotBlank() && mobileNoStr.length == 10) {

                applyAndVerifyMobileNo(mobileNoStr)
            } else {
                Snackbar.make(binding.signInParentLay, resources.getString(R.string.enter_validnumber), Snackbar.LENGTH_LONG).show()
            }
        } else {
            val otpStr: String = binding.enterOTPET.text.toString()

            verifyOTP(mobileNoStr, otpStr)
        }
    }
}