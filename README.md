# CovidTracer

### Screenshots
![All screens](https://firebasestorage.googleapis.com/v0/b/covidtracer-7a0f6.appspot.com/o/CovidTracer%2FScreenshots%2FAllScreens.v2.png?alt=media&token=87299887-2d5c-420c-9af6-487f45370c18)

### How it works
After you sign in, you get an OTP token generated using Firebase Phone Auth (currently the phone number is hardcoded for Romanian prefixes, but this is easily changeable). After you login, the application starts a background service that constantly publishes and receives the Firestore Database UIDs, by using the Nearby Messages API from Google. When two devices are in close proximity (approximately 4-5m for Bluetooth + Sonar, didn't test max distance since I can't go any further than my own home due to social distancing), their meetup is registered in Firestore.
From the picker in the logged in screen, you can choose your current health status and press the button. This updates your health status in the database. Using Firestore Cloud Messages, there is a JavaScript function that triggers when this update happens and sends a push notification to the users that you have interacted with.

### Backend
- https://firebase.google.com/docs/firestore/quickstart
- https://firebase.google.com/docs/functions/get-started
- https://developers.google.com/nearby/messages/overview
- https://cloud.google.com/maps-platform/

Create or update your local <b>gradle.properties</b> file with: \
The API_KEY for the Nearby Messages API (<b>API_KEY=apikeyexample</b>) \
The MAPS_API_KEY for the Google Maps SDK for Android (<b>MAPS_API_KEY=mapsapikeyexample</b>).


### Components
- Nearby Messages API       (contact tracing)
- Firestore                 (database)
- Firebase Functions        (serverless code)
- Firebase Authentication   (authenticate requests)

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
    
### Credits
- https://github.com/nicolaa5 - for refactoring the code and adding the location information