//const socket = new WebSocket("ws://193.48.125.70:50007");
//const socket = new WebSocket("ws://193.48.125.64:8888");

const socket = new WebSocket("ws:127.0.0.1:8888");

const joystick = document.querySelector('#static');
const footer = document.querySelector('#joystick-footer');

let serverOpened = false;

const manager = nipplejs.create(
    {
        zone: document.getElementById('static'),
        mode: 'static',
        position: {
            left: '50%',
            top: '50%'
        },
        color: 'orange',
        size: 200
    }
);

// GESTION SOCKET

// Le client est ouvert
socket.onopen = (event) => {
    let init = { "type":"init","infoInit":"Client-->Server  demande de connexion","clientName":"Client42","clientType":"autre","mdp":"123"};
    socket.send(JSON.stringify(init));
    serverOpened = true;
};


// Le serveur a envoyé des messages
socket.onmessage = (event) => {
    let data = JSON.parse(event.data);
    console.log(data);
}


// GESTION JOYSTICK
/*
manager.on('move', function(event, data) {
	console.log(data);
});*/

    manager.on('dir', (event, data) => {
        let manuel = document.querySelector('#manuel');
        if (manuel.checked && serverOpened) {
            let json = 
                {
                    "type" : "commandeRobotino",
                    "commande" : "setPositions",
                    "data": {
                        "x": data.position.x,
                        "y": data.position.y,
                        "angle": {
                            "degree": data.angle.degree,
                            "radian": data.angle.radian
                        },
                    },
                    "destinataire" : {
                        "name" : "Server Robotino v1",
                        "ip" : "193.48.125.70:50007"
                    },
                    "expediteur" : {
                        "name" : "C1",
                        "ip" : "0.0.0.0"
                    }
                };
            socket.send(JSON.stringify(json));
        }
    });

