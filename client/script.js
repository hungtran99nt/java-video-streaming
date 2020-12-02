let button = document.querySelector("#connect");
button.addEventListener("click", (e) => {
	let connection = new WebSocket("ws://127.0.0.1:6437");
	console.log("Waitting...");
	connection.onopen = () => {
		console.log("Connected!");
		connection.send("Hello there");
	};

	connection.onerror = (error) => {
		console.log("WebSocket error: " + error.data);
	};

	connection.onmessage = (message) => {
		console.log("Server: " + message);
	};
});
