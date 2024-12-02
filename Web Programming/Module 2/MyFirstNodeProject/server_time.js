// createServer is passed an arrow function that takes two arguments passed in by Node.
// Inrequest contains info about HTTP method (e.g. GET), url, and headers.
// Inresponse contains the status code, data to be sent to the user, and can also close connection.
require("http").createServer((inRequest, inResponse) => {
    // Import request module, which allows sending of HTTP requests to external URLS and receive responses
    // Variable requestModule represents the whole library. Analogous to an import statement. Variable represents the whole request library.
    const requestModule = require("request");
    // Constructor. Pass URL and an anonymous callback function
    // Must invoke the function the module (requestModule) represents.
    requestModule(
        "http://worldtimeapi.org/api/timezone/America/New_York",
        // Makes a GET request to the API and returns the result in inBody
        // Inerr/inresp contain error information and response metadata(http status)
        (inErr, inResp, inBody) => {
            inResponse.end(`Hello from my first Node Web server: ${inBody}`);
        }
    );
   }).listen(80);