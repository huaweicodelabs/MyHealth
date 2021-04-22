## My-Health-sample

## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction
    The sample code is used to implement the function of reading step count using the Awareness and Health kit sdk. 
    The following describes the structure of the sample code:

	activities: UI, which contains the courses related information.
	adapters  : Adapter of the different screens.
	database  : It consist of classes related to database tables and database helper classes.
	dialogs   : A class used to show dialogs.
	fragments : Consists of multiple fragments that are used in different activates.
	models    : Data model classes.
	utils     : A tool class.

## Installation
    To use functions provided by examples, please make sure Huawei Mobile Service 5.0 has been installed on your cellphone. 
    There are two ways to install the sample demo:

    You can compile and build the codes in Android Studio. After building the APK, you can install it on the phone and debug it.
    Generate the APK file from Gradle. Use the ADB tool to install the APK on the phone and debug it adb install 
    {YourPath}\app\release\app-release.apk
    
## Supported Environments
	Android Studio 3.X, JDK 1.8 and later , SDK Platform 24 and later, Gradle 4.6 and later

	
## Configuration 
    Create an app in AppGallery Connect and obtain the project configuration file agconnect-services.json. 
    In Android Studio, switch to the Project view and move the agconnect-services.json file to the root directory of the app.

    Change the value of applicationId in the build.gradle file of the app to the name of the app package applied for in the preceding step.
	
## Sample Code
   
    The My Health -Demo provides demonstration for following scenarios:

    1. Awareness kit integrated and it will always used to recognize the user behaviour.
    2. Integrated the RoomDB and initialized in Application class. Query, add and update operations are performed,
       In the app whole data is fetching from RoomDB.
    3. Integrated the Health Kit and with the help of awareness kit, Health kit will read users step count.
    4. Integrated few common kits like Analytics,Auth service and Push kit. 
       
##  License
    My Health sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).