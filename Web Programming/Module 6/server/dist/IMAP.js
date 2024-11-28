"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Worker = void 0;
const ImapClient = require("emailjs-imap-client");
const mailparser_1 = require("mailparser");
// Executes when module loads. Necessary to make calls to IMAP server work.
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
// IMAP worker class
// Server information is passed into constructor
class Worker {
    constructor(inServerInfo) {
        Worker.serverInfo = inServerInfo;
    }
    // Creates imap client object and connects it to the server
    connectToServer() {
        return __awaiter(this, void 0, void 0, function* () {
            // Client is created
            const client = new ImapClient.default(Worker.serverInfo.imap.host, Worker.serverInfo.imap.port, {
                auth: Worker.serverInfo.imap.auth,
                useSecureTransport: true,
                requireTLS: true
            });
            client.logLevel = client.LOG_LEVEL_NONE;
            client.onerror = (inError) => {
                console.log("IMAP.Worker.listMailboxes(): Connection error", inError);
            };
            yield client.connect();
            console.log("IMAP.Worker.listMailboxes(): Connected");
            return client;
        });
    }
    // List mailboxes
    // Get a client using connectToServer
    listMailboxes() {
        return __awaiter(this, void 0, void 0, function* () {
            const client = yield this.connectToServer();
            // Grab the mailboxes using a client method (not this function, but same name)
            const mailboxes = yield client.listMailboxes();
            yield client.close();
            const finalMailboxes = [];
            // Recursive call to grab all the children 
            const iterateChildren = (inArray) => {
                inArray.forEach((inValue) => {
                    finalMailboxes.push({
                        name: inValue.name, path: inValue.path
                    });
                    iterateChildren(inValue.children);
                });
            };
            iterateChildren(mailboxes.children);
            return finalMailboxes;
        });
    }
    listMessages(inCallOptions) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log("IMAP.Worker.listMessages()", inCallOptions);
            const client = yield this.connectToServer();
            // We have to select the mailbox first.  This gives us the message count.
            const mailbox = yield client.selectMailbox(inCallOptions.mailbox);
            console.log(`IMAP.Worker.listMessages(): Message count = ${mailbox.exists}`);
            // If there are no messages then just return an empty array.
            if (mailbox.exists === 0) {
                yield client.close();
                return [];
            }
            // Okay, there are messages, let's get 'em!  Note that they are returned in order by uid, so it's FIFO.
            // inCallOptions contains name of mailbox in the .mailbox field
            const messages = yield client.listMessages(
            // Grab msgs beginning with first one and all others after it
            // Just grabbing the unique id (uid) and metadata (envelope). Not grabbing msg body.
            inCallOptions.mailbox, "1:*", ["uid", "envelope"]);
            yield client.close();
            // Translate from emailjs-imap-client message objects to app-specific objects.
            const finalMessages = [];
            messages.forEach((inValue) => {
                finalMessages.push({
                    id: inValue.uid,
                    date: inValue.envelope.date,
                    from: inValue.envelope.from[0].address,
                    subject: inValue.envelope.subject
                });
            });
            return finalMessages;
        });
    } /* End listMessages(). */
    // Grab the actual body of a message
    getMessageBody(inCallOptions) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log("IMAP.Worker.getMessageBody()", inCallOptions);
            const client = yield this.connectToServer();
            // Call list messages again, but this time specify we want the body
            const messages = yield client.listMessages(
            // Specifying message we want by its id, so we have to add a fourth param
            inCallOptions.mailbox, inCallOptions.id, ["body[]"], { byUid: true });
            // Does some processing on the grabbed messages
            const parsed = yield (0, mailparser_1.simpleParser)(messages[0]["body[]"]);
            yield client.close();
            return parsed.text;
        });
    }
    // Telling client we want to delete a message by id.
    deleteMessage(inCallOptions) {
        return __awaiter(this, void 0, void 0, function* () {
            const client = yield this.connectToServer();
            yield client.deleteMessages(inCallOptions.mailbox, inCallOptions.id, { byUid: true });
            yield client.close();
        });
    }
}
exports.Worker = Worker;
//# sourceMappingURL=IMAP.js.map