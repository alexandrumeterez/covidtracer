# CovidTracer
Contact tracing app used ONLY during the epidemic of the COVID-19 coronavirus disease.
I designed this app to help the romanian people to overcome this crisis faster by 
giving them a chance to inform others automatically of their status.

### Requirements
- Firebase duh
- Android
- Java
- maybe other stuff

### JSON schema for Firebase

2 tabels:
- users
    ```
    "users": {
        "user_id1" : {
            "name" : 
            "surname" : 
            "email" : 
            "phone" :
        }
        "user_id2" : {
            "name" : 
            "surname" : 
            "email" : 
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
### TODO
1. Make basic app - done
2. Connect to Firebase - done
3. Add users to Firebase - done
4. Write code to collect user meetings - done
5. Write code for health status change and Firestore callbacks to send push notifications 
6. Work on design and other fluff (field restrictions, formatting, better looking app etc)
Important considerations: what to use as <b> user_id </b> to be unique?

