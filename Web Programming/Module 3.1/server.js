const fs = require("fs");
const path = require("path");

// Create an HTTP server
require("http").createServer((inRequest, inResponse) => {
    const requestedUrl = inRequest.url === "/" ? "/index.html" : inRequest.url;
    // Dirname is a global variable that contains the absolute path of the currently executed script
    const filePath = path.join(__dirname, requestedUrl);

    // Determine content type based on file extension
    const extension = path.extname(filePath);
    const contentType = {
        ".html": "text/html",
        ".js": "application/javascript",
        ".css": "text/css",
        ".json": "application/json",
        ".png": "image/png",
        ".jpg": "image/jpeg",
        ".gif": "image/gif",
    }[extension] || "application/octet-stream";

    // Read and serve the requested file
    fs.readFile(filePath, (err, data) => {
        if (err) {
            inResponse.writeHead(404, { "Content-Type": "text/plain" });
            inResponse.end("Error: File not found");
        } else {
            inResponse.writeHead(200, { "Content-Type": contentType });
            inResponse.end(data);
        }
    });
}).listen(80, () => {
    console.log("Server is running on http://localhost");
});
