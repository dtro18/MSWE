"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const path_1 = __importDefault(require("path"));
const express_1 = __importDefault(require("express"));
const ServerInfo_1 = require("./ServerInfo");
const IMAP = __importStar(require("./IMAP"));
const SMTP = __importStar(require("./SMTP"));
const Contacts = __importStar(require("./Contacts"));
// Creates expres App
const app = (0, express_1.default)();
// Add middleware that parses incoming request bodies that contain JSON
app.use(express_1.default.json());
// Basic web server
app.use("/", 
// Static middleware is built-in to serve static resources
express_1.default.static(path_1.default.join(__dirname, "../../client/dist")));
// 
app.use(function (inRequest, inResponse, inNext) {
    // Allow all requests to Express server
    inResponse.header("Access-Control-Allow-Origin", "*");
    // Determine what HTTP methods we're going to allow
    inResponse.header("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
    // Tell CORS what addtl headers to accept
    inResponse.header("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
    inNext();
});
// Register the GET method path
// Resource we get back is a list of mailboxes, hence /mailboxes.
// All app.xxx calls (where xxx is an http method) require a callback function to execute
// Making some asynchronous calls using await, so must throw in async
app.get("/mailboxes", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        // IMAP worker object is instantiated
        const imapWorker = new IMAP.Worker(ServerInfo_1.serverInfo);
        // listMailboxes called, stored in an mailboxes array of type Imailbox
        const mailboxes = yield imapWorker.listMailboxes();
        console.log("GET /mailboxes (1): Ok", mailboxes);
        inResponse.json(mailboxes);
    }
    catch (inError) {
        console.log("Logging in console.");
        console.log("Logging in console.", inError); // Log the error itself
        inResponse.status(500).send("Error 1234");
    }
}));
// Get a list of messages from a specific mailbox
// :mailbox is a replacement token, indicating dynamic value that we want to present to our code as a request param
app.get("/mailboxes/:mailbox", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        // IMAP worker object is instantiated
        const imapWorker = new IMAP.Worker(ServerInfo_1.serverInfo);
        // Messages array is created by querying the messages of a specific mailbox, provided by request
        const messages = yield imapWorker.listMessages({
            // Accessing that dynamic value of mailbox
            mailbox: inRequest.params.mailbox
        });
        inResponse.json(messages);
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.get("/mailboxes/:mailbox/:id", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        // IMAP worker object is instantiated
        const imapWorker = new IMAP.Worker(ServerInfo_1.serverInfo);
        const messageBody = yield imapWorker.getMessageBody({
            mailbox: inRequest.params.mailbox,
            id: parseInt(inRequest.params.id, 10)
        });
        inResponse.json(messageBody);
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.delete("/mailboxes/:mailbox/:id", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        // IMAP worker object is instantiated
        const imapWorker = new IMAP.Worker(ServerInfo_1.serverInfo);
        yield imapWorker.deleteMessage({
            mailbox: inRequest.params.mailbox,
            id: parseInt(inRequest.params.id, 10)
        });
        inResponse.send("ok");
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.post("/messages", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        // SMTP worker object is instantiated
        const smtpWorker = new SMTP.Worker(ServerInfo_1.serverInfo);
        // Incoming request body will contain all the info we need, including target email, subject, message text
        // Express.json middleware will parse that into an object to pass into sendMessage()
        yield smtpWorker.sendMessage(inRequest.body);
        inResponse.send("ok");
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.get("/contacts", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const contactsWorker = new Contacts.Worker();
        const contacts = yield contactsWorker.listContacts();
        inResponse.json(contacts);
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.post("/contacts", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const contactsWorker = new Contacts.Worker();
        // Pass in the request's info about the new contact to be added
        const contacts = yield contactsWorker.addContact(inRequest.body);
        // Return the added contact, including an ID which we need to access that contact again
        inResponse.json(contacts);
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
app.delete("/contacts/:id", (inRequest, inResponse) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const contactsWorker = new Contacts.Worker();
        // Pass in the id of the contact we're trying to delete
        yield contactsWorker.deleteContact(inRequest.params.id);
        inResponse.send("ok");
    }
    catch (inError) {
        inResponse.send("error");
    }
}));
// Start app listening.
app.listen(80, () => {
    console.log("MailBag server open for requests");
});
//# sourceMappingURL=main.js.map