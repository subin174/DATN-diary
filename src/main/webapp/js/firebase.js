const config = {
    apiKey: "AIzaSyAahjkXSvmOBjlrrVpb3-eyRA3OQs2A5h0",
    authDomain: "nhatro-743a3.firebaseapp.com",
    projectId: "nhatro-743a3",
    storageBucket: "nhatro-743a3.appspot.com",
    messagingSenderId: "673663738684",
    databaseURL: "https://nhatro-743a3.firebaseio.com",
    appId: "1:673663738684:web:bdee53a411a046e59b7439",
    measurementId: "G-E04WLMRGG8"
};

// const config = {
//     apiKey: "AIzaSyBICyFq4GPWKBhCNe46-1MCsDNbfoymkU0",
//     authDomain: "amazing-project-95629.firebaseapp.com",
//     databaseURL: "https://amazing-project-95629.firebaseio.com",
//     projectId: "amazing-project-95629",
//     storageBucket: "amazing-project-95629.appspot.com",
//     messagingSenderId: "42388728204"
// };

firebase.initializeApp(config);


const database = firebase.database();

database.ref('/').once('value', function (snapshot) {
    console.log(snapshot.val());
});


setDataRef = database.ref("/setData");
setDataRef.on('child_changed', function (snapshot) {
    // console.log("Below is the data from child_changed");
    console.log(snapshot.data);
});

// var rootRef = database.ref('/');
//
// rootRef.once('value', function(snapshot){
//     console.log(snapshot.val());
// });