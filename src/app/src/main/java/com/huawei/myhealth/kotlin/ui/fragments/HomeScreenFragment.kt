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

import android.Manifest
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.huawei.hmf.tasks.OnCompleteListener
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.hihealth.AutoRecorderController
import com.huawei.hms.hihealth.HiHealthOptions
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.Field
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.hihealth.options.OnSamplePointListener
import com.huawei.hms.kit.awareness.Awareness
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier
import com.huawei.hms.kit.awareness.barrier.BarrierStatus
import com.huawei.hms.kit.awareness.barrier.BehaviorBarrier
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult
import com.huawei.myhealth.R
import com.huawei.myhealth.adapter.HomeRecyclerViewAdapter
import com.huawei.myhealth.kotlin.app.MyHealthApplication
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.entity.StepsWalked
import com.huawei.myhealth.database.viewmodel.FoodIntakeViewModel
import com.huawei.myhealth.databinding.FragmentHomeScreenBinding
import com.huawei.myhealth.model.HomeDataModel
import com.huawei.myhealth.utils.AddBarrierForAwareness
import com.huawei.myhealth.utils.CommonUtil
import com.huawei.myhealth.utils.DefaultValues
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @since 2020
 * @author Huawei DTSE India
 */

class HomeScreenFragment : Fragment() {
    private val TAG = "HomeScreen"
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private val mPermissionsOnHigherVersion = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION)
    private val mPermissionsOnLowerVersion = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            "com.huawei.hms.permission.ACTIVITY_RECOGNITION")
    private var mPendingIntent: PendingIntent? = null
    private var mBarrierReceiver: BehaviorBarrierReceiver? = null
    private var controller: AutoRecorderController? = null
    private var mSettingController: SettingController? = null
    var stepCount = "0"
    var selectedDateStr: String? = null
    private var adapter: HomeRecyclerViewAdapter? = null
    // creating an arraylist to store users using the data class user
    val homeDataModelList = ArrayList<HomeDataModel>()
    var glass_consumed = 0
    var steps_walked = "0 "
    var cal_burned = "0 "
    private val calorieTakenTodayList: MutableList<Int>? = ArrayList()
    var cal_taken = 0

    private val foodIntakeViewModel: FoodIntakeViewModel by viewModels {
        FoodIntakeViewModel.FoodIntakeViewModelFactory((activity?.application as MyHealthApplication).foodIntakeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        //initialize Awareness kit
        initAwarness()
        // login for health kit
        signInForHealthKit()
        // initilize
        init(view)
        // Receiving Data Via Observer
        foodIntakeViewModel.calorieTakenToday.observe(viewLifecycleOwner) { foodIntake ->
            foodIntake.let {
                for (calorieInEachFood in foodIntake) {
                    calorieTakenTodayList?.add(calorieInEachFood.energy)
                }

                if (calorieTakenTodayList != null) {
                    cal_taken = calorieTakenTodayList.sum()
                    populateDataForHomeScreenItems(calorieTakenTodayList.sum())
                } else {
                    populateDataForHomeScreenItems(DefaultValues.CAL_DEFAULT)
                }
            }
        }

        // get the data from db
        getDataFromDB()

        return view
    }
    //Method to Initilize
    private fun init(view: View) {
        // adding data to the list
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.calorie_taken), cal_taken.toString() + (resources.getString(R.string.CAL_TAKEN_DEFAULT)
                 ), R.drawable.ic_local_dining_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.water_consumed), glass_consumed.toString() + (resources.getString(R.string.GLASSESS_DEFAULT)), R.drawable.ic_opacity_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.steps_count), steps_walked + (resources.getString(R.string.STEPS_DEFAULT)), R.drawable.ic_directions_run_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.calorie_burned), (resources.getString(R.string.Burned)
                ) + cal_burned + (resources.getString(R.string.CAL_DEFAULT)), R.drawable.ic_fitness_center_24px))
        // creating our adapter
        adapter = HomeRecyclerViewAdapter(homeDataModelList, this)
        // now adding the adapter to recyclerview
        binding.homeMenuRecyclerView.adapter = adapter

        val manager = GridLayoutManager(context, DefaultValues.SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        binding.homeMenuRecyclerView.setLayoutManager(manager)
        // calender
        //set current date to calendar
        val simpleDateFormat = SimpleDateFormat(resources.getString(R.string.date_pattern))
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding.tvDate.setText(currentDateAndTime)
        // add current date
        val sharedPreference = activity?.getSharedPreferences("MY_HEALTH", Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat(resources.getString(R.string.date_pattern))
        selectedDateStr = sdf.format(Date())
        var editor = sharedPreference?.edit()
        editor?.putString(resources.getString(R.string.date_selected), selectedDateStr)
        editor?.commit()
        //Action Datepicker
        binding.rlCalendar!!.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            //action for datepicker
            val datepickerdialog: DatePickerDialog = activity?.let { it1 ->
                DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = resources.getString(R.string.date_pattern) // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    // Display Selected date in textbox
                    binding.tvDate.setText(""+sdf.format(cal.time))
                    selectedDateStr = ""+sdf.format(cal.time)
                    //Update the date in sharedpreference
                    var editor = sharedPreference?.edit()
                    editor?.putString(resources.getString(R.string.date_selected), selectedDateStr)
                    editor?.commit()
                    getDataFromDB()
                    // Receiving Data Via Observer
                    foodIntakeViewModel.calorieTakenToday.observe(viewLifecycleOwner) { foodIntake ->
                        foodIntake.let {
                            for (calorieInEachFood in foodIntake) {
                                calorieTakenTodayList?.add(calorieInEachFood.energy)
                            }

                            if (calorieTakenTodayList != null) {
                                cal_taken = calorieTakenTodayList.sum()
                                populateDataForHomeScreenItems(calorieTakenTodayList.sum())
                            } else {
                                populateDataForHomeScreenItems(DefaultValues.CAL_DEFAULT)
                            }
                        }
                    }
                }, y, m, d)
            }!!

            datepickerdialog.show()
        }
    }

    //override method to destroy activity
    override fun onDestroyView() {
        super.onDestroyView()

        if (mBarrierReceiver != null) {
            activity?.unregisterReceiver(mBarrierReceiver)
        }
        _binding = null
    }
    //populate the data from db
    private fun populateDataForHomeScreenItems(calorieTakenToday: Int) {

        updateUI()

        calorieTakenTodayList?.clear()
    }
    //Initialize the Awareness kit
    private fun initAwarness() {
        //check for the Awareness kit support
        queryDeviceSupportingCapabilities()
        //check for the permissions
        checkAndRequestPermissions()
        //check for the behaviour and add barrier
        checkBehavior()
        //receiver class initialization
        registerReceiverClass()
    }
    //check for the Awareness kit support
    private fun queryDeviceSupportingCapabilities() {
        // Use querySupportingCapabilities to query awareness capabilities supported by the current device.
        activity?.let {
            Awareness.getCaptureClient(it).querySupportingCapabilities().addOnSuccessListener { capabilityResponse ->
                        val status = capabilityResponse.capabilityStatus
                        val capabilities = status.capabilities
                        val deviceSupportingStr = StringBuilder()
                        deviceSupportingStr.append("This device supports the following awareness capabilities:\n")
                        for (capability in capabilities) {
                            // deviceSupportingStr.append(Constant.CAPABILITIES_DESCRIPTION_MAP.get(capability));
                            deviceSupportingStr.append("\n")
                        }

                        // Toast.makeText(activity, deviceSupportingStr.toString(), Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                         }
        }
    }
    //check for the permissions
    private fun checkAndRequestPermissions() {
        val permissionsDoNotGrant: MutableList<String> = java.util.ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (permission in mPermissionsOnHigherVersion) {
                if (activity?.let { ActivityCompat.checkSelfPermission(it, permission) }
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission)
                }
            }
        } else {
            for (permission in mPermissionsOnLowerVersion) {
                if (activity?.let { ActivityCompat.checkSelfPermission(it, permission) }
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission)
                }
            }
        }
        if (permissionsDoNotGrant.size > 0) {
            activity?.let {
                ActivityCompat.requestPermissions(it,
                        permissionsDoNotGrant.toTypedArray(), DefaultValues.PERMISSION_REQUEST_CODE)
            }
        }
    }
    //check for the behaviour and add barrier
    private fun checkBehavior() {
        activity?.let {
            Awareness.getCaptureClient(it).behavior
                    .addOnSuccessListener { behaviorResponse ->
                        val behaviorStatus = behaviorResponse.behaviorStatus
                        val mostLikelyBehavior = behaviorStatus.mostLikelyBehavior
                    }
                    .addOnFailureListener { e ->
                        CommonUtil.logger(e.toString(),TAG)
                    }
        }
    }
    //receiver class initialization
    private fun registerReceiverClass() {
        val barrierReceiverAction: String = (activity?.getApplication()?.getPackageName() ?: "") + resources.getString(R.string.BEHAVIOR_ACTION)
        val intent = Intent(barrierReceiverAction)
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(activity, DefaultValues.DEFAULT_VALUE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = BehaviorBarrierReceiver()
        activity?.registerReceiver(mBarrierReceiver, IntentFilter(barrierReceiverAction))
        var beginWalkingBarrier: AwarenessBarrier? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            beginWalkingBarrier = BehaviorBarrier.beginning(BehaviorBarrier.BEHAVIOR_WALKING)
        }
        //Add the beginning barrier to listen user behaviour changes
        activity?.let { AddBarrierForAwareness.addBarrier(it, resources.getString(R.string.BEGINNING_BARRIER_LABEL), beginWalkingBarrier, mPendingIntent) }
    }
    //receiver class initialization and listen for user behavior changes
    inner class BehaviorBarrierReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Here will get the status of barrier
            val barrierStatus = BarrierStatus.extract(intent)
            val label = barrierStatus.barrierLabel
            val barrierPresentStatus = barrierStatus.presentStatus
            when (label) {
                resources.getString(R.string.BEGINNING_BARRIER_LABEL)-> if (barrierPresentStatus == BarrierStatus.TRUE) {
                    binding.tvStatus.setText(activity!!.getText(R.string.user_status_start_walking))
                    binding.tvStatus.setText(activity!!.getText(R.string.user_status_walking))
                    CommonUtil.logger(resources.getString(R.string.startwalk),TAG)
                    //User start to walk
                    callToStartStepCount()
                }
                resources.getString(R.string.ENDING_BARRIER_LABEL)  -> if (barrierPresentStatus == BarrierStatus.TRUE) {
                    binding.tvStatus.setText(activity!!.getText(R.string.user_status_stop_walking))
                    CommonUtil.logger(resources.getString(R.string.stopwalking),TAG)
                    //User stops walking
                    callToStopStepCount()
                }
                else
                {
                    binding.tvStatus.setText(activity!!.getText(R.string.user_status))
                }
            }
        }
    }

    // Create the data callback object onSamplePointListener to print the step count data.
    private val onSamplePointListener = OnSamplePointListener {
        samplePoint ->
        for (field in samplePoint.dataType.fields) {
            //step count will be returned in this listner
            stepCount = samplePoint.getFieldValue(Field.FIELD_STEPS).toString()
            //Update the step count
            callToUpdateStepCount(stepCount)

        }
    }
   //Methods to start Read the step count
    private fun callToStartStepCount() {
        val dataType = DataType.DT_CONTINUOUS_STEPS_TOTAL
        controller?.startRecord(dataType, onSamplePointListener)?.addOnCompleteListener(OnCompleteListener<Void?> { taskResult -> // Handling of callback exceptions needs to be added for the case that the calling fails due to the app not being authorized or type not being supported.
            if (taskResult.isSuccessful) {
                //start Reading the step count is successfull
                CommonUtil.logger(resources.getString(R.string.start_stepcount_success),TAG)
            } else {
                //start Reading the step count is failed
                CommonUtil.logger(resources.getString(R.string.start_stepcount_failed),TAG)
            }
        })?.addOnSuccessListener(OnSuccessListener<Void?> {
            //startRecordByType Successful
        })?.addOnFailureListener(OnFailureListener { e ->
            //startRecordByType Failed
        })
    }
    //Method to stop Read the step count
    private fun callToStopStepCount() {
        controller?.stopRecord(DataType.DT_CONTINUOUS_STEPS_TOTAL, onSamplePointListener)
                ?.addOnCompleteListener(OnCompleteListener<Void?> { taskResult -> // Handling of callback exceptions needs to be added for the case that the calling fails due to the app not being authorized or type not being supported.
                    if (taskResult.isSuccessful) {
                        //stop Reading the step count is successfull
                        CommonUtil.logger(resources.getString(R.string.stop_stepcount_success),TAG)
                    } else {
                        //stop Reading the step count is failed
                        CommonUtil.logger(resources.getString(R.string.stop_stepcount_failed),TAG)
                    }

                })
                ?.addOnSuccessListener(OnSuccessListener<Void?> {
                    //stopRecordByType Successful
                })
                ?.addOnFailureListener(OnFailureListener { e ->
                    //stopRecordByType failed
                })
       //Update the step count in db
        callToUpdateStepCount(stepCount)
    }

    //Update the step data to db
    private fun callToUpdateStepCount(stepCount: String) {
        //strore the data in shared preferences
        val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var uid = sharedPreference?.getString(resources.getString(R.string.UID), "")
        var dateselected = sharedPreference?.getString(resources.getString(R.string.date_selected), "")
        val stepsWalked = StepsWalked(uid.toString(), dateselected.toString(), stepCount)
        //db initialization
        var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        var newRecord = true
        // insert to db
        val thread = Thread {
            // fetch Records
            db?.stepsWalkedDao()?.getStepsWalkedData()?.forEach()
            {
                if (it.date.equals(dateselected)) {
                    newRecord = false
                }
            }
            if (newRecord) {
                //insert Records
                db?.stepsWalkedDao()?.insert(stepsWalked)
            } else {
                //update Records
                db?.stepsWalkedDao()?.update(stepCount, dateselected)
            }
        }
        thread.start()
        thread.interrupt()
    }
   //Method to signInForHealthKit
    private fun signInForHealthKit() {
        val options = HiHealthOptions.builder().build()
        val signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(options)
        mSettingController = activity?.let { HuaweiHiHealth.getSettingController(it, signInHuaweiId) }
        controller = activity?.let { HuaweiHiHealth.getAutoRecorderController(it, signInHuaweiId) }
        val scopeList: List<Scope> = java.util.ArrayList()
        // Add scopes to apply for. The following only shows an example.
        // Developers need to add scopes according to their specific needs.
        scopeList.toMutableList().add(Scope(Scopes.HEALTHKIT_STEP_BOTH))// View and save step counts in HUAWEI Health Kit.
        scopeList.toMutableList().add(Scope(Scopes.HEALTHKIT_HEIGHTWEIGHT_BOTH)) // View and save height and weight in HUAWEI Health Kit.
        scopeList.toMutableList().add(Scope(Scopes.HEALTHKIT_HEARTRATE_BOTH)) // View and save the heart rate data in HUAWEI Health Kit.
        // Configure authorization parameters.
        val authParamsHelper = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
        val authParams = authParamsHelper.setIdToken().setAccessToken().setScopeList(scopeList).createParams()
        // Initialize the HuaweiIdAuthService object.
        val authService = HuaweiIdAuthManager.getService(activity, authParams)
        // Silent sign-in. If authorization has been granted by the current account,
        // the authorization screen will not display. This is an asynchronous method.
        val authHuaweiIdTask = authService.silentSignIn()
        // Add the callback for the call result.
        authHuaweiIdTask.addOnSuccessListener { huaweiId: AuthHuaweiId? ->
            // The silent sign-in is successful.
            Toast.makeText(activity, resources.getString(R.string.silentSignIn_success), Toast.LENGTH_LONG).show()
             checkOrAuthorizeHealth()

        }.addOnFailureListener { exception ->
            // The silent sign-in fails.
            // This indicates that the authorization has not been granted by the current account.
            if (exception is ApiException) {
                val apiException = exception as ApiException
                // Call the sign-in API using the getSignInIntent() method.
                val signInIntent = authService.signInIntent
                // Display the authorization screen by using the startActivityForResult() method of the activity.
                // Developers can change HealthKitAuthClientActivity to the actual activity.
                startActivityForResult(signInIntent, DefaultValues.REQUEST_SIGN_IN_LOGIN)
            }
        }
    }

    //Method to checkOrAuthorizeHealth
    private fun checkOrAuthorizeHealth() {
        // Check the Health authorization status. If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
        val authTask: Task<Boolean> = mSettingController!!.healthAppAuthorisation
        authTask.addOnSuccessListener(object : OnSuccessListener<Boolean?> {
            override fun onSuccess(result: Boolean?) {
                TODO("Not yet implemented")
                if (java.lang.Boolean.TRUE == result) {
                    //checkOrAuthorizeHealth get result success.Initialize the awareness kit
                    initAwarness()
                } else {
                    // If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
                    val healthKitSchemaUri: Uri = Uri.parse(activity?.getString(R.string.HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME))
                    val intent = Intent(Intent.ACTION_VIEW, healthKitSchemaUri)
                    // Check whether the authorization screen of the Health app can be displayed.
                    if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                        // Display the authorization screen by using the startActivityForResult() method of the activity.
                        // You can change HihealthKitMainActivity to the actual activity.
                        startActivityForResult(intent, DefaultValues.REQUEST_HEALTH_AUTH)
                    }
                }
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(exception: Exception?) {
                if (exception != null) {
                    CommonUtil.logger(exception.message.toString(), TAG)
                }
            }
        })
    }
   //Override method to handle the results
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle the sign-in response.
        handleSignInResult(requestCode, data)

        // Process the response returned from the Health authorization screen.
        handleHealthAuthResult(requestCode);
    }
   //Method to handle Auth result
    private fun handleHealthAuthResult(requestCode: Int) {
        if (requestCode != DefaultValues.REQUEST_HEALTH_AUTH) {
            return
        }
        //Query and check the Health Auth
        queryHealthAuthorization()
    }

    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data Authorization result response.
     */
    private fun handleSignInResult(requestCode: Int, data: Intent?) {
        // Handle only the authorized responses
        if (requestCode != DefaultValues.REQUEST_SIGN_IN_LOGIN) {
            return
        }

        // Obtain the authorization response from the intent.
        val result: HuaweiIdAuthResult = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data)
        if (result.isSuccess()) {
            CommonUtil.logger(resources.getString(R.string.silentSignIn_success),TAG)
            // Obtain the authorization result.
            checkOrAuthorizeHealth()
        }
    }

    /**
     * Check whether the authorization is successful.
     */
    private fun queryHealthAuthorization() {
        val queryTask = mSettingController!!.healthAppAuthorisation
        queryTask.addOnSuccessListener { result ->
            if (java.lang.Boolean.TRUE == result) {
                //queryHealthAuthorization get result success.Initialize the awareness kit
                initAwarness()
            }
        }.addOnFailureListener { exception ->
            if (exception != null) {
                CommonUtil.logger(exception.message.toString(),TAG)
            }
        }
    }
   //Method to get the data from db
    private fun getDataFromDB() {
        var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, "my_health_database").build() }
        // getData from db
        val thread = Thread {
            // fetch Records
            //get water intake data from db
            db?.waterIntakeDao()?.getWaterIntakeData()?.forEach()
            {
                if (selectedDateStr.equals(it.date)) {
                    glass_consumed = it.noOfGlassConsumed
                } else {
                    glass_consumed = DefaultValues.DEFAULT_VALUE
                }
            }
          //get step count data from db
            db?.stepsWalkedDao()?.getStepsWalkedData()?.forEach()
            {
                if (selectedDateStr.equals(it.date)) {
                    steps_walked = it.stepsWalked
                } else {
                    steps_walked = DefaultValues.DEFAULT_VALUE.toString()
                }
            }
            //get excercise data from db
            db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
            {
                if (selectedDateStr.equals(it.dateNTime)) {
                    cal_burned = it.calorieBurned
                } else {
                    cal_burned = DefaultValues.DEFAULT_VALUE.toString()
                    cal_taken = DefaultValues.DEFAULT_VALUE
                }
            }
            //update the data in UI
            updateUI()
        }
        thread.start()
        thread.interrupt()
    }
    //Method to update the data in UI
    private fun updateUI() {
        //clear list keep it empty to add new data
        homeDataModelList.clear()
        // updating data to the list
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.calorie_taken), "${cal_taken.toString()}"+(resources.getString(R.string.CAL_TAKEN_DEFAULT)), R.drawable.ic_local_dining_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.water_consumed), glass_consumed.toString()  + (resources.getString(R.string.GLASSESS_DEFAULT)), R.drawable.ic_opacity_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.steps_count), steps_walked + (resources.getString(R.string.STEPS_DEFAULT)), R.drawable.ic_directions_run_24px))
        homeDataModelList.add(HomeDataModel(resources.getString(R.string.calorie_burned), (resources.getString(R.string.Burned)
                )  + cal_burned + (resources.getString(R.string.CAL_DEFAULT)), R.drawable.ic_fitness_center_24px))
       //refresh the UI after the data is added
        try {
            if (adapter != null) {
                activity?.runOnUiThread(Runnable {
                    // Stuff that updates the UI
                    adapter!!.notifyDataSetChanged()
                })
            }
        } catch (e: java.lang.NullPointerException) {
            CommonUtil.logger(e.toString(),TAG)
        }
        catch (e: java.lang.IllegalStateException) {
            CommonUtil.logger(e.toString(),TAG)
        }
    }


}

