const socket = new WebSocket("ws://193.48.125.70:50007");
const joystick = document.querySelector('#static');
const footer = document.querySelector('#joystick-footer');

const manager = nipplejs.create(
    {
        zone: document.getElementById('static'),
        mode: 'static',
        position: {
            left: '50%',
            top: '50%'
        },
        color: 'blue',
        size: 200
    }
);

// GESTION SOCKET

// Le client est ouvert
socket.onopen = (event) => {
    let init = { "type":"init","infoInit":"Client-->Server  demande de connexion","clientName":"Client42","clientType":"autre","mdp":"123"};
    socket.send(JSON.stringify(init));
};

// Le serveur a envoyé des messages
socket.onmessage = (event) => {
    let data = JSON.parse(event.data);
    footer.innerHTML = `<p>Position x : ${data.x}, position y : ${data.y}`;
}


// GESTION JOYSTICK
/*
manager.on('move', function(event, data) {
	console.log(data);
});*/

manager.on('dir', (event, data) => {
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
    console.log(json);
    // socket.send(JSON.stringify(json))
});
