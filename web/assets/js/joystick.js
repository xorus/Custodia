let manager = nipplejs.create(
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

/*
manager.on('move', function(event, data) {
	console.log(data);
});*/

manager.on('dir', function(event, data) {
	console.log(data);
});