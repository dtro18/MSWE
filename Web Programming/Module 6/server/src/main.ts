import path from "path";
import express,
{ Express, NextFunction, Request, Response } from "express";
import { serverInfo } from "./ServerInfo";
import * as IMAP from "./IMAP";
import * as SMTP from "./SMTP";
import * as Contacts from "./Contacts";
import { IContact } from "./Contacts";

// Creates expres App
const app: Express = express();
// Add middleware that parses incoming request bodies that contain JSON
app.use(express.json());
// Basic web server
app.use("/",
    // Static middleware is built-in to serve static resources
    express.static(path.join(__dirname, "../../client/dist"))
);

// 
app.use(function(inRequest: Request, inResponse: Response,
    inNext: NextFunction) {
    // Allow all requests to Express server
    inResponse.header("Access-Control-Allow-Origin", "*");
    // Determine what HTTP methods we're going to allow
    inResponse.header("Access-Control-Allow-Methods",
    "GET,POST,DELETE,OPTIONS"
    );
    // Tell CORS what addtl headers to accept
    inResponse.header("Access-Control-Allow-Headers",
    "Origin,X-Requested-With,Content-Type,Accept"
    );
    inNext();
});

// Register the GET method path
// Resource we get back is a list of mailboxes, hence /mailboxes.
// All app.xxx calls (where xxx is an http method) require a callback function to execute
// Making some asynchronous calls using await, so must throw in async
app.get("/mailboxes",
    async (inRequest: Request, inResponse: Response) => {
        try {
            // IMAP worker object is instantiated
            const imapWorker: IMAP.Worker = new IMAP.Worker(serverInfo);
            // listMailboxes called, stored in an mailboxes array of type Imailbox
            const mailboxes: IMAP.IMailbox[] = await imapWorker.listMailboxes();
            console.log("GET /mailboxes (1): Ok", mailboxes);
            inResponse.json(mailboxes);
        } catch (inError) {
            console.log("Logging in console.")
            console.log("Logging in console.", inError); // Log the error itself
            inResponse.status(500).send("Error 1234");
        }
    }
);
// Get a list of messages from a specific mailbox
// :mailbox is a replacement token, indicating dynamic value that we want to present to our code as a request param
app.get("/mailboxes/:mailbox",
    async (inRequest: Request, inResponse: Response) => {
        try {
            // IMAP worker object is instantiated
            const imapWorker: IMAP.Worker = new IMAP.Worker(serverInfo);
            // Messages array is created by querying the messages of a specific mailbox, provided by request
            const messages: IMAP.IMessage[] = await imapWorker.listMessages({
                // Accessing that dynamic value of mailbox
                mailbox : inRequest.params.mailbox
            });
            inResponse.json(messages);
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

app.get("/mailboxes/:mailbox/:id",
    async (inRequest: Request, inResponse: Response) => {
        try {
            // IMAP worker object is instantiated
            const imapWorker: IMAP.Worker = new IMAP.Worker(serverInfo);
            const messageBody: string | undefined = await imapWorker.getMessageBody({
                mailbox: inRequest.params.mailbox,
                id: parseInt(inRequest.params.id, 10)
            })
            inResponse.json(messageBody);
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

app.delete("/mailboxes/:mailbox/:id",
    async (inRequest: Request, inResponse: Response) => {
        try {
            // IMAP worker object is instantiated
            const imapWorker: IMAP.Worker = new IMAP.Worker(serverInfo);
            await imapWorker.deleteMessage({
                mailbox: inRequest.params.mailbox,
                id: parseInt(inRequest.params.id, 10)
            })
            inResponse.send("ok");
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

app.post("/messages",
    async (inRequest: Request, inResponse: Response) => {
        try {
            // SMTP worker object is instantiated
            const smtpWorker: SMTP.Worker = new SMTP.Worker(serverInfo);
            // Incoming request body will contain all the info we need, including target email, subject, message text
            // Express.json middleware will parse that into an object to pass into sendMessage()
            await smtpWorker.sendMessage(inRequest.body)
            inResponse.send("ok");
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

app.get("/contacts",
    async (inRequest: Request, inResponse: Response) => {
        try {
            const contactsWorker: Contacts.Worker = new Contacts.Worker();
            const contacts: IContact[] = await contactsWorker.listContacts();
            inResponse.json(contacts);
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

app.post("/contacts",
    async (inRequest: Request, inResponse: Response) => {
        try {
            const contactsWorker: Contacts.Worker = new Contacts.Worker();
            // Pass in the request's info about the new contact to be added
            const contacts: IContact = await contactsWorker.addContact(inRequest.body);
            // Return the added contact, including an ID which we need to access that contact again
            inResponse.json(contacts);
        } catch (inError) {
        inResponse.send("Error adding contact");
        }
    }
);

app.delete("/contacts/:id",
    async (inRequest: Request, inResponse: Response) => {
        try {
            const contactsWorker: Contacts.Worker = new Contacts.Worker();
            // Pass in the id of the contact we're trying to delete
            await contactsWorker.deleteContact(inRequest.params.id);
            inResponse.send("ok");
        } catch (inError) {
        inResponse.send("error");
        }
    }
);

// Start app listening.
app.listen(80, () => {
    console.log("MailBag server open for requests");
});