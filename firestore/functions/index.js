const functions = require('firebase-functions');
var admin = require("firebase-admin");

admin.initializeApp();
exports.sendPushNotification = functions.firestore
    .document('users/{userID}')
    .onUpdate((change, context) => {
        const userID = context.params.userID;
        let collectionRef = admin.firestore().collection('users_meetings');
        let documentRefWithName = collectionRef.doc(userID).collection('meetings');
        console.log(`Reference with name: ${documentRefWithName.path}`);
        let ids;
        return documentRefWithName.get().then(s=>{
            docs = s.docs;
            ids = docs.map(doc => doc.id);
            return ids;
        }).then((ids)=>{
            console.log(ids);
            return ids.forEach((e)=>{
                let usersRef = admin.firestore().collection('users');
                let documentRef = usersRef.doc(e);
                return documentRef.get().then(s=>{
                    var token = s.data().token;
                    console.log(token);
                    var message = {
                        data : {
                            oldStatus: change.before.data().status,
                            newStatus: change.after.data().status
                        },
                        token: token
                    };
                    return admin.messaging().send(message)
                    .then((response) => {
                        return console.log("Sent message to ", token);
                    })
                    .catch((error) => {
                        return console.log('Error sending message', error);
                    });
                });
            });            
        });
        
    });
