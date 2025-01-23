# WatchDog Library - Beta Release 🚀

A lightweight crash tracking library for Android, designed to simplify crash monitoring and reporting.

🎉 Beta Features
✔️ Crash Tracking: Automatically captures app crashes.
✔️ Firebase Crashlytics Recording: Logs crash reports to Firebase Crashlytics.

## What's Coming Next?
The next release will introduce:

Detailed Crash Reports: Sending more comprehensive crash details to Firebase Crashlytics.
Customizable Crash Dialogs: Allowing you to personalize the message shown to users after a crash.
Configurable Behavior: Options to exit the app or keep it running after a crash.

## How to Integrate (Beta)

Step 1: Add Dependency
Add this to your app's build.gradle:

gradle

dependencies {
 
    implementation 'com.github.muhammad-ahmed-lib:watchdog:beta-1.0.0'

}

Step 2: Initialize WatchDog

Add the following code in your Application class or main activity:


val watchDogBehaviour = WatchDogBehaviour.EXIST_APP

val watchDogEventListener = object : WatchDogEventListener() {
   
    override fun onErrorCaptured(report: CrashReport) {
      
        Log.d("WatchDog", "onErrorCaptured: $report")
   
    }
}

WatchDogActivityBuilder(
   
    appContext = this,
  
    behaviour = watchDogBehaviour,
   
    watchDogEventListener = watchDogEventListener,
   
    title = "App Crashed",
   
    message = "Sorry, we are working to fix this error.",
   
    crashlytics = FirebaseCrashlytics.getInstance()
)
## How It Works
Crash Tracking: The library monitors crashes in your app.
Crash Recording: Crash data is sent directly to Firebase Crashlytics.
Customizable Dialog (Coming Soon): Inform users about crashes with personalized messages.
## Future Updates
The upcoming release will focus on:

Enhanced crash report details.
User-friendly customization options.
App behavior control after crashes.
## Feedback & Support
This is a beta release, and your feedback is crucial for improvements. Feel free to:

Share your thoughts.
Report bugs or issues.
Suggest features you'd like to see.
💌 Email: ahmed03160636141@gmail.com

⚙️ License
MIT License
