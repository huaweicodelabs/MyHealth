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

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hihealth.AutoRecorderController;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.hihealth.data.DataType;
import com.huawei.hms.hihealth.data.Field;
import com.huawei.hms.hihealth.data.SamplePoint;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.hihealth.options.OnSamplePointListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.barrier.AwarenessBarrier;
import com.huawei.hms.kit.awareness.barrier.BarrierStatus;
import com.huawei.hms.kit.awareness.barrier.BehaviorBarrier;
import com.huawei.hms.kit.awareness.capture.BehaviorResponse;
import com.huawei.hms.kit.awareness.capture.CapabilityResponse;
import com.huawei.hms.kit.awareness.status.BehaviorStatus;
import com.huawei.hms.kit.awareness.status.CapabilityStatus;
import com.huawei.hms.kit.awareness.status.DetectedBehavior;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentHomeScreenBinding;
import com.huawei.myhealth.java.adapter.HomeRecyclerViewAdapter;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.ExerciseDone;
import com.huawei.myhealth.java.database.entity.FoodIntake;
import com.huawei.myhealth.java.database.entity.StepsWalked;
import com.huawei.myhealth.java.database.entity.WaterIntake;
import com.huawei.myhealth.java.database.viewmodel.FoodIntakeViewModel;
import com.huawei.myhealth.java.model.HomeDataModel;
import com.huawei.myhealth.java.utils.AddBarrierForAwarness;
import com.huawei.myhealth.java.utils.CommonUtil;
import com.huawei.myhealth.java.utils.DefaultValues;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.huawei.myhealth.java.utils.DefaultValues.PERMISSION_REQUEST_CODE;
import static com.huawei.myhealth.java.utils.DefaultValues.REQUEST_HEALTH_AUTH;
/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class HomeScreenFragment extends Fragment {
    boolean newRecord = true;
    private FragmentHomeScreenBinding binding;
    private final String TAG = "HomeScreen";
    private final String[] mPermissionsOnHigherVersion = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.ACTIVITY_RECOGNITION"};
    private final String[] mPermissionsOnLowerVersion = new String[]{"android.permission.ACCESS_FINE_LOCATION", "com.huawei.hms.permission.ACTIVITY_RECOGNITION"};
    private PendingIntent mPendingIntent;
    private BehaviorBarrierReceiver mBarrierReceiver;
    private AutoRecorderController controller;
    private SettingController mSettingController;
    private String stepCount = "0";
    private String selectedDateStr="";
    private HomeRecyclerViewAdapter adapter;
    private  ArrayList homeDataModelList = new ArrayList();
    private int glass_consumed;
    private String steps_walked = "0 ";
    private String cal_burned = "0 ";
    private final List calorieTakenTodayList = (List)(new ArrayList());
    private int cal_taken;
    private AutoRecorderController autoRecorderController;
    MyHealthDatabase db;

    private FoodIntakeViewModel foodIntakeViewModel;



    final String BEGINNING_BARRIER_LABEL=getResources().getString(R.string.BEGINNING_BARRIER_LABEL);

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);

        View view = binding.getRoot();
        foodIntakeViewModel = new ViewModelProvider(this).get(FoodIntakeViewModel.class);

        //initialize Awareness kit
        initAwarness();

        // login for health kit
        signInForHealthKit();

        // initilize
        init(view);

        foodIntakeViewModel.calorieTakenToday().observe(getViewLifecycleOwner(), foodIntakes -> {
                    for (FoodIntake calorieInEachFood : foodIntakes) {
                        calorieTakenTodayList.add(calorieInEachFood.getEnergy());
                    }

                    if (calorieTakenTodayList != null) {
                        cal_taken = HomeScreenFragment.calulateList(calorieTakenTodayList);
                        //cal_taken = calorieTakenTodayList.sum();
                        //populateDataForHomeScreenItems(calorieTakenTodayList.sum());
                        populateDataForHomeScreenItems(HomeScreenFragment.calulateList(calorieTakenTodayList));
                    } else {
                        populateDataForHomeScreenItems(DefaultValues.CAL_DEFAULT);
                    }
                }
        );

        // get the data from db
        getDataFromDB();

        return view;
    }
    //Initialize the Awareness kit
    private void init(View view)
    {
        //db initialization
         db = MyHealthDatabase.getDatabase(getActivity());

        // adding data to the list
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.calorie_taken),(String.valueOf(cal_taken)+(getResources().getString(R.string.CAL_TAKEN_DEFAULT))),R.drawable.ic_local_dining_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.water_consumed),(String.valueOf(glass_consumed)+(getResources().getString(R.string.GLASSESS_DEFAULT))),R.drawable.ic_opacity_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.steps_count),(String.valueOf(steps_walked)+(getResources().getString(R.string.STEPS_DEFAULT))),R.drawable.ic_directions_run_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.calorie_burned),(getResources().getString(R.string.Burned)
        )+(String.valueOf(cal_burned)+(getResources().getString(R.string.CAL_DEFAULT))),R.drawable.ic_fitness_center_24px));

        GridLayoutManager manager = new GridLayoutManager(this.getContext(), DefaultValues.SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        binding.homeMenuRecyclerView.setLayoutManager((RecyclerView.LayoutManager)manager);
        // creating our adapter
        adapter = new HomeRecyclerViewAdapter(homeDataModelList, this);
        // now adding the adapter to recyclerview
        binding.homeMenuRecyclerView.setAdapter(adapter);

        // calender
        //set current date to calendar
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
        String currentDateAndTime = simpleDateFormat.format(new Date());
        binding.tvDate.setText(currentDateAndTime);
        // add current date
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("MY_HEALTH", Context.MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
        selectedDateStr = sdf.format(new Date());
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(getResources().getString(R.string.date_selected), selectedDateStr);
        editor.commit();

        //Action Datepicker
        binding.rlCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int y = cal.get(1);
                int m = cal.get(2);
                int d = cal.get(5);
                //action for datepicker
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        cal.set(Calendar.YEAR, i);
                        cal.set(Calendar.MONTH, i1);
                        cal.set(Calendar.DAY_OF_MONTH, i2);
                        String myFormat = getResources().getString(R.string.date_pattern);
                        // mention the format you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        // Display Selected date in textbox
                        binding.tvDate.setText(""+sdf.format(cal.getTime()));
                        selectedDateStr = ""+sdf.format(cal.getTime());
                        //Update the date in sharedpreference
                        SharedPreferences.Editor editor = sharedPreference.edit();
                        editor.putString(getResources().getString(R.string.date_selected), selectedDateStr);
                        editor.commit();
                        getDataFromDB();

                        foodIntakeViewModel.calorieTakenToday().observe(getViewLifecycleOwner(), foodIntakes -> {
                            for (FoodIntake calorieInEachFood : foodIntakes) {
                                calorieTakenTodayList.add(calorieInEachFood.getEnergy());
                            }

                            if (calorieTakenTodayList != null) {
                                //cal_taken = calorieTakenTodayList.sum();
                                //populateDataForHomeScreenItems(calorieTakenTodayList.sum());
                                cal_taken = HomeScreenFragment.calulateList(calorieTakenTodayList);
                                populateDataForHomeScreenItems(HomeScreenFragment.calulateList(calorieTakenTodayList));
                            } else {
                                populateDataForHomeScreenItems(DefaultValues.CAL_DEFAULT);
                            }
                        });
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        }

        );
    }
    //override method to destroy activity
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBarrierReceiver != null) {
            getActivity().unregisterReceiver(mBarrierReceiver);
        }
        binding = null;
    }

    //populate the data from db
    private void populateDataForHomeScreenItems(int calorieTakenToday) {
        updateUI();
        calorieTakenTodayList.clear();
    }

    //Initialize the Awareness kit
    private void initAwarness() {
        //check for the Awareness kit support
        queryDeviceSupportingCapabilities();
        //check for the permissions
        checkAndRequestPermissions();
        //check for the behaviour and add barrier
        checkBehavior();
        //receiver class initialization
        registerReceiverClass();
    }
    //check for the Awareness kit support
    private void queryDeviceSupportingCapabilities() {
        // Use querySupportingCapabilities to query awareness capabilities supported by the current device.
        Awareness.getCaptureClient(getActivity()).querySupportingCapabilities()
                .addOnSuccessListener(new OnSuccessListener<CapabilityResponse>() {
                    @Override
                    public void onSuccess(CapabilityResponse capabilityResponse) {
                        CapabilityStatus status = capabilityResponse.getCapabilityStatus();
                        int[] capabilities = status.getCapabilities();
                        Log.i(TAG, "capabilities code :" + Arrays.toString(capabilities));
                        StringBuilder deviceSupportingStr = new StringBuilder();
                        deviceSupportingStr.append("This device supports the following awareness capabilities:\n");
                        for (int capability : capabilities) {
                            //  deviceSupportingStr.append(Constant.CAPABILITIES_DESCRIPTION_MAP.get(capability));
                            deviceSupportingStr.append("\n");
                        }
                        Toast.makeText(getActivity(), deviceSupportingStr.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Failed to get supported capabilities"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //check for the permissions
    private void checkAndRequestPermissions() {
        List<String> permissionsDoNotGrant = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (String permission : mPermissionsOnHigherVersion) {
                if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission);
                }
            }
        } else {
            for (String permission : mPermissionsOnLowerVersion) {
                if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsDoNotGrant.add(permission);
                }
            }
        }

        if (permissionsDoNotGrant.size() > 0) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionsDoNotGrant.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }
    //check for the behaviour and add barrier
    private void checkBehavior() {
        Awareness.getCaptureClient(getActivity()).getBehavior()
                .addOnSuccessListener(new OnSuccessListener<BehaviorResponse>() {
                    @Override
                    public void onSuccess(BehaviorResponse behaviorResponse) {
                        BehaviorStatus behaviorStatus = behaviorResponse.getBehaviorStatus();
                        DetectedBehavior mostLikelyBehavior = behaviorStatus.getMostLikelyBehavior();
                        String str = "Most likely behavior type is " + mostLikelyBehavior.getType() +
                                ",the confidence is " + mostLikelyBehavior.getConfidence();
                        CommonUtil.logger(str,TAG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        CommonUtil.logger(e.toString(),TAG);
                    }
                });
    }
    //receiver class initialization
    private void registerReceiverClass() {
        final String barrierReceiverAction = getActivity().getApplication().getPackageName() + "BEHAVIOR_BARRIER_RECEIVER_ACTION";
        Intent intent = new Intent(barrierReceiverAction);
        // You can also create PendingIntent with getActivity() or getService().
        // This depends on what action you want Awareness Kit to trigger when the barrier status changes.
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register a broadcast receiver to receive the broadcast sent by Awareness Kit when the barrier status changes.
        mBarrierReceiver = new BehaviorBarrierReceiver();
        getActivity().registerReceiver(mBarrierReceiver, new IntentFilter(barrierReceiverAction));

        AwarenessBarrier beginWalkingBarrier = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            beginWalkingBarrier = BehaviorBarrier.beginning(BehaviorBarrier.BEHAVIOR_WALKING);
        }
        AddBarrierForAwarness.addBarrier(getActivity(), getResources().getString(R.string.BEGINNING_BARRIER_LABEL), beginWalkingBarrier, mPendingIntent);

    }
    //receiver class initialization and listen for user behavior changes
    final class BehaviorBarrierReceiver extends BroadcastReceiver {

        static final String BEGINNING_BARRIER_LABEL="behavior beginning barrier label";
        static final String ENDING_BARRIER_LABEL="behavior ending barrier label";

        @Override
        public void onReceive(Context context, Intent intent) {
            BarrierStatus barrierStatus = BarrierStatus.extract(intent);
            String label = barrierStatus.getBarrierLabel();
            int barrierPresentStatus = barrierStatus.getPresentStatus();
            switch (label) {


                case BEGINNING_BARRIER_LABEL:
                    if (barrierPresentStatus == BarrierStatus.TRUE) {
                        binding.tvStatus.setText(getResources().getText(R.string.user_status_start_walking));
                        binding.tvStatus.setText(getResources().getText(R.string.user_status_walking));
                        CommonUtil.logger(getResources().getString(R.string.startwalk),TAG);
                        //User start to walk
                        callToStartStepCount();
                    }
                    break;

                case ENDING_BARRIER_LABEL:
                    binding.tvStatus.setText(getActivity().getText(R.string.user_status_stop_walking));
                    CommonUtil.logger(getResources().getString(R.string.stopwalking),TAG);
                    //User stops walking
                    callToStopStepCount();
                    break;

                default:
                    binding.tvStatus.setText(getActivity().getText(R.string.user_status));
                    break;
            }

        }
    }
    // Create the data callback object onSamplePointListener to print the step count data.
    private OnSamplePointListener onSamplePointListener = new OnSamplePointListener() {
        @Override
        public void onSamplePoint(SamplePoint samplePoint) {
            //step count will be returned in this listner
            stepCount = samplePoint.getFieldValue(Field.FIELD_STEPS).toString();
            //Update the step count
            callToUpdateStepCount(stepCount);


        }
    };
   //Method to start Reading step count
    private void callToStartStepCount()
    {
        if (autoRecorderController == null) {
            initAutoRecorderController();
        }

        // Start recording real-time steps.
        autoRecorderController.startRecord(DataType.DT_CONTINUOUS_STEPS_TOTAL, new OnSamplePointListener() {
            @Override
            public void onSamplePoint(SamplePoint samplePoint) {
                // The step count, time, and type data reported by the pedometer is called back to the app through
                // samplePoint.
                stepCount = samplePoint.getFieldValue(Field.FIELD_STEPS).toString();
                //Update the step count
                callToUpdateStepCount(stepCount);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //start Reading the step count is successfull
                CommonUtil.logger(getResources().getString(R.string.start_stepcount_success),TAG);
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //start Reading the step count is failed
                        CommonUtil.logger(getResources().getString(R.string.start_stepcount_failed),TAG);
                    }
                });
    }
   //Method to stopReadingstepcount
    private void callToStopStepCount()
    {
        // Start recording real-time steps.
        autoRecorderController.startRecord(DataType.DT_CONTINUOUS_STEPS_TOTAL, new OnSamplePointListener() {
            @Override
            public void onSamplePoint(SamplePoint samplePoint) {
                // The step count, time, and type data reported by the pedometer is called back to the app through
                // samplePoint.
                stepCount = samplePoint.getFieldValue(Field.FIELD_STEPS).toString();
                //Update the step count
                callToUpdateStepCount(stepCount);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //stop Reading the step count is successfull
                CommonUtil.logger(getResources().getString(R.string.stop_stepcount_success),TAG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //stop Reading the step count is failed
                CommonUtil.logger(getResources().getString(R.string.stop_stepcount_failed),TAG);
            }
        });
        //Update the step count in db
        callToUpdateStepCount(stepCount);
    }
    //Method to signInForHealthKit
    private void signInForHealthKit()
    {
        HiHealthOptions options = HiHealthOptions.builder().build();
        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(options);
        autoRecorderController =
                HuaweiHiHealth.getAutoRecorderController(getActivity(), signInHuaweiId);

        List<Scope> scopeList = new ArrayList<>();

        // Add scopes to apply for. The following only shows an example. You need to add scopes according to your specific needs.
        scopeList.add(new Scope(Scopes.HEALTHKIT_STEP_BOTH)); // View and save step counts in HUAWEI Health Kit.
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEIGHTWEIGHT_BOTH)); // View and save height and weight in HUAWEI Health Kit.
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEARTRATE_BOTH)); // View and save the heart rate data in HUAWEI Health Kit.

        // Configure authorization parameters.
        HuaweiIdAuthParamsHelper authParamsHelper = new HuaweiIdAuthParamsHelper(
                HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
        HuaweiIdAuthParams authParams = authParamsHelper.setIdToken()
                .setAccessToken()
                .setScopeList(scopeList)
                .createParams();
        // Initialize the HuaweiIdAuthService object.
        final HuaweiIdAuthService authService = HuaweiIdAuthManager.getService(getActivity(),
                authParams);

        // Silent sign-in. If authorization has been granted by the current account, the authorization screen will not display. This is an asynchronous method.
        Task<AuthHuaweiId> authHuaweiIdTask = authService.silentSignIn();

        // Add the callback for the call result.
        authHuaweiIdTask.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId huaweiId) {
                // The silent sign-in is successful.
                Toast.makeText(getActivity(), getActivity().getString(R.string.silentSignIn_success), Toast.LENGTH_LONG).show();
                checkOrAuthorizeHealth();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // The silent sign-in fails. This indicates that the authorization has not been granted by the current account.
                Intent signInIntent = authService.getSignInIntent();
                startActivityForResult(signInIntent, com.huawei.myhealth.java.utils.DefaultValues.REQUEST_SIGN_IN_LOGIN);

            }
        });
    }
    //Method to checkOrAuthorizeHealth
    private void checkOrAuthorizeHealth()
    {
// Check the Health authorization status. If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
        Task<Boolean> authTask = mSettingController.getHealthAppAuthorisation();
        authTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    initAwarness();
                } else {
                    // If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
                    Uri healthKitSchemaUri = Uri.parse(getActivity().getString(R.string.HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME));
                    Intent intent = new Intent(Intent.ACTION_VIEW, healthKitSchemaUri);
                    // Check whether the authorization screen of the Health app can be displayed.
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Display the authorization screen by using the startActivityForResult() method of the activity.
                        // You can change HihealthKitMainActivity to the actual activity.
                        startActivityForResult(intent, REQUEST_HEALTH_AUTH);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                if (exception != null) {
                    CommonUtil.logger(exception.getMessage().toString(), TAG);
                }
            }
        });
    }
    //Override method to handle the results
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the sign-in response.
        handleSignInResult(requestCode, data);

        // Process the response returned from the Health authorization screen.
        handleHealthAuthResult(requestCode);
    }
    //Method to handle Auth result
    private void handleHealthAuthResult(int requestCode) {
        if (requestCode != DefaultValues.REQUEST_HEALTH_AUTH) {
            return;
        }
        //Query and check the Health Auth
        queryHealthAuthorization();
    }
    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data Authorization result response.
     */
    private void handleSignInResult(int requestCode , Intent data) {
        // Handle only the authorized responses
        if (requestCode != DefaultValues.REQUEST_SIGN_IN_LOGIN) {
            return;
        }

        // Obtain the authorization response from the intent.
        HuaweiIdAuthResult result = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
        if (result.isSuccess()) {
            CommonUtil.logger(getResources().getString(R.string.silentSignIn_success),TAG);
            // Obtain the authorization result.
            checkOrAuthorizeHealth();
        }
    }
    /**
     * Check whether the authorization is successful.
     */
    private void queryHealthAuthorization() {
        Task<Boolean> queryTask = mSettingController.getHealthAppAuthorisation();
        queryTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    //queryHealthAuthorization get result success.Initialize the awareness kit
                    initAwarness();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                if (exception != null) {
                    CommonUtil.logger(exception.getMessage().toString(),TAG);
                }
            }
        });
    }
    //Method to initialize autocontroller
    private void initAutoRecorderController() {
        HiHealthOptions options = HiHealthOptions.builder().build();
        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(options);
        autoRecorderController = HuaweiHiHealth.getAutoRecorderController(getActivity(), signInHuaweiId);
        mSettingController = HuaweiHiHealth.getSettingController(getActivity(), signInHuaweiId);
    }
    //Method to Update the stepcount
    private void callToUpdateStepCount(String stepcount)
    {
        //strore the data in shared preferences

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
        String uid = sharedPreference.getString(getResources().getString(R.string.UID), "");
        String dateselected = sharedPreference.getString(getResources().getString(R.string.date_selected), "");
        StepsWalked stepsWalked = new StepsWalked(uid.toString(), dateselected.toString(), stepCount);

        // insert to db
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<StepsWalked> list = db.stepsWalkedDao().getStepsWalkedData();
                for(int i=0; i<list.size(); i++) {
                  if(list.get(i).getDate().equals(dateselected))
                  {
                      newRecord = false;
                  }
                }
                if (newRecord) {
                    //insert Records
                    db.stepsWalkedDao().insert(stepsWalked);
                } else {
                    //update Records
                    db.stepsWalkedDao().update(stepCount, dateselected);
                }
            }
        }
        );
        thread.start();
        thread.interrupt();
    }
    //Method to get the data from db
    private void getDataFromDB()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // fetch Records
                //get water intake data from db
                List<WaterIntake> list_waterIntake = db.waterIntakeDao().getWaterIntakeData();
                for(int i=0; i<list_waterIntake.size(); i++) {
                    if(list_waterIntake.get(i).getDate().equals(selectedDateStr))
                    {
                        glass_consumed = list_waterIntake.get(i).getNoOfGlassConsumed();
                    }
                    else {
                        glass_consumed = DefaultValues.DEFAULT_VALUE;
                    }
                }

                //get step count data from db
                List<StepsWalked> list_stepWalked = db.stepsWalkedDao().getStepsWalkedData();
                for(int i=0; i<list_stepWalked.size(); i++) {
                    if(list_stepWalked.get(i).getDate().equals(selectedDateStr))
                    {
                        steps_walked = list_stepWalked.get(i).getStepsWalked();
                    }
                    else {
                        steps_walked = String.valueOf(DefaultValues.DEFAULT_VALUE);
                    }
                }

                //get excercise data from db
                List<ExerciseDone> list_excerciseDone = db.excerciseDoneDao().getExerciseDoneData();
                for(int i=0; i<list_excerciseDone.size(); i++) {
                    if(list_excerciseDone.get(i).getDateNTime().equals(selectedDateStr))
                    {
                        cal_burned = list_excerciseDone.get(i).getCalorieBurned();
                    }
                    else {
                        cal_burned = String.valueOf(DefaultValues.DEFAULT_VALUE);
                        cal_taken = DefaultValues.DEFAULT_VALUE;
                    }
                }

                //update the data in UI
                updateUI();

            }
        });
        thread.start();
        thread.interrupt();



    }
    //Method to update the data in UI
    private void updateUI() {
        //clear list keep it empty to add new data
        homeDataModelList.clear();
        // updating data to the list
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.calorie_taken), String.valueOf(cal_taken) +(getResources().getString(R.string.CAL_TAKEN_DEFAULT)), R.drawable.ic_local_dining_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.water_consumed), String.valueOf(glass_consumed)  +" "+ (getResources().getString(R.string.GLASSESS_DEFAULT)), R.drawable.ic_opacity_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.steps_count), steps_walked + " "+(getResources().getString(R.string.STEPS_DEFAULT)), R.drawable.ic_directions_run_24px));
        homeDataModelList.add(new HomeDataModel(getResources().getString(R.string.calorie_burned), (" "
        )  + cal_burned + " "+ (getResources().getString(R.string.CAL_DEFAULT)), R.drawable.ic_fitness_center_24px));
        //refresh the UI after the data is added
        try {
            if (adapter != null) {
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       // Stuff that updates the UI
                       adapter.notifyDataSetChanged();
                   }
               });
            }
        } catch (NullPointerException e) {
            CommonUtil.logger(e.toString(),TAG);
        }
        catch (IllegalStateException e) {
            CommonUtil.logger(e.toString(),TAG);
        }
    }

    private static int calulateList(List getInputList) {
        int sumValue = 0;
        for (int i = 0; i < getInputList.size(); i++) {
            sumValue = sumValue + (int) getInputList.get(i);
        }
        return sumValue;
    }
}