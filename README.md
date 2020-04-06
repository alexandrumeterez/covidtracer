# CovidTracer HackTheCrisis Sweden

### Screenshots
![All screens](https://firebasestorage.googleapis.com/v0/b/covidtracer-7a0f6.appspot.com/o/CovidTracer%2FScreenshots%2FAllScreens.v2.png?alt=media&token=87299887-2d5c-420c-9af6-487f45370c18)

### Hackathon Android APK build 0.1.0 (Debug)
Here is a link to the .apk build for the hackathon that you can install on your Android device. 
https://firebasestorage.googleapis.com/v0/b/covidtracer-7a0f6.appspot.com/o/CovidTracer%2FApp%20builds%2Fapp-debug-0.1.0.apk?alt=media&token=4889cab0-f1e7-4656-aff9-c5656ca911c0

### Video
Here is a short video demonstration of the Hackathon build when the health status of one user has been modified: 
https://firebasestorage.googleapis.com/v0/b/covidtracer-7a0f6.appspot.com/o/CovidTracer%2FVideo%2FTwo%20devices%20demonstration.MOV?alt=media&token=1c5b4a07-90ab-48c9-ad65-00d797cc1a2e

### How it works
After you sign in, you get an OTP token generated using Firebase Phone Auth. After you login, the application starts a background service that constantly publishes and receives the Firestore Database UIDs, by using the Nearby Messages API from Google. When two devices are in close proximity (approximately 4-5m for Bluetooth + sonar) their meetup is registered in Firestore.

In the logged in screen, you can choose your current health status and press the button. This updates your health status in the database. Using Firestore Cloud Messages, there is a JavaScript function that triggers when this update happens and sends a push notification to the users that you have interacted with. 

### Frontend Configuration (Android App)
Follow the instructions for setting up your Google maps SDK for Android here: 
https://developers.google.com/maps/documentation/android-sdk/start

Follow the instructions for interacting with the Google Nearby Messages API: 
https://developers.google.com/nearby/messages/android/get-started

Create or update your local <b>gradle.properties</b> file with: \
The API_KEY for the Nearby Messages API (<b>API_KEY=apikeyexample</b>) \
The MAPS_API_KEY for the Google maps SDK for Android (<b>MAPS_API_KEY=mapsapikeyexample</b>).

### Backend Configuration (Firestore / Functions / Authentication)
Follow the installation guide for Firestore \
https://firebase.google.com/docs/firestore/quickstart

Set up Firebase Functions \
https://firebase.google.com/docs/functions/get-started

Once you complete the Firebase Functions setup your can navigate to ```./firestore/functions ```
and run ```npm install``` or ```yarn``` to install the node dependencies. Then navigate back to  ```./firestore ``` and run 
```firebase deploy --only functions```. A push notification is set up in -> ```./firestore/functions/index.js ``` which can be configured to send data from one device to another using Firestore Database UIDs.

### Components
- Android codebase in Java
- Nearby Messages API       (contact tracing)
- Firestore                 (database)
- Firebase Functions        (serverless code)
- Firebase Authentication   (authenticate requests)


### Other info
- Developed with Android Studio verion -> ```Android 3.6.2```

### JSON schema for Firebase

2 tabels:
- users
    ```
    "users": {
        "user_id1" : {
            "phone" :
        }
        "user_id2" : {
            "phone" :
        }
    }
    ```

- users meetings
    ```
    "users_meetings" : {
        "user_id1" : {
            "meetings" : {
                "user_id_met1" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
                "user_id_met2" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
            }
        }
        "user_id2" : {
            "meetings" : {
                "user_id_met1" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
                "user_id_met2" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
            }
        }
    }
    ```
    
### Fork of contact tracing project
Project based on Alexander Drumeterez's covidtracer
https://github.com/alexandrumeterez/covidtracer
