"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.serverInfo = void 0;
const path = require("path");
const fs = require("fs");
const rawInfo = fs.readFileSync(path.join(__dirname, "../serverInfo.json"));
exports.serverInfo = JSON.parse(rawInfo);
//  curl -d "{ "to" : "jlozada0910@gmail.com", "from" : "dtro18@gmail.com", "subject" : "This is a test", "text" : "the n word" }" -H "Content-Type:application/json" -X POST localhost/messages
//# sourceMappingURL=ServerInfo.js.map