// Require returns an object that is an API provided by the module (http).
// Argument passed is a request listener, which is executed any time a request is mdae to the server.
// Create server returns a reference to a webserver instance, which has the method listen().
// Once listen is called, the server begins listening for requests.

// Create server is the callback function and receives two args, inRequest and inResponse, which are objects that represent the http request and response.
// Callback function calls the end() method on the inResponse object.
require("http").createServer((inRequest, inResponse) => {
    inResponse.end("Hello from my first Node Web server");
   }).listen(80);