const socket = new WebSocket("ws://localhost:8888");
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
    console.log("Je demande une connexion");
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
    console.log(data);
	socket.send(JSON.stringify(data.position));
});