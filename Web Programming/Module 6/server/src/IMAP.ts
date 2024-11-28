
import { ParsedMail } from "mailparser";
const ImapClient = require("emailjs-imap-client");
import { simpleParser } from "mailparser";
import { IServerInfo } from "./ServerInfo";

// Call options (list mailboxes, list messages, retrieve message, delete message) must have mailbox, but list mailboxes doesn't require id.
export interface ICallOptions {
    mailbox: string,
    id?: number
}

// Used when listing and retrieving messages. Listing doesn't require the body of the message.
export interface IMessage {
    id: string, 
    date: string,
    from: string,
    subject: string, 
    body?: string
}

// Name is what shows on the string, path is how code identifies a mailbox
export interface IMailbox { name: string, path: string }

// Executes when module loads. Necessary to make calls to IMAP server work.
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0"

// IMAP worker class
// Server information is passed into constructor
export class Worker {
    private static serverInfo: IServerInfo;
    constructor(inServerInfo: IServerInfo) {
        Worker.serverInfo = inServerInfo;
    }
    // Creates imap client object and connects it to the server
    private async connectToServer(): Promise<any> {
        // Client is created
        const client: any = new ImapClient.default(
            Worker.serverInfo.imap.host,
            Worker.serverInfo.imap.port,
            { 
                auth : Worker.serverInfo.imap.auth,
                useSecureTransport: true,
                requireTLS: true
             }
        );
        client.logLevel = client.LOG_LEVEL_NONE;
        client.onerror = (inError: Error) => {
            console.log("IMAP.Worker.listMailboxes(): Connection error", inError);
        };
        await client.connect();
        console.log("IMAP.Worker.listMailboxes(): Connected");
        return client;
    }

    // List mailboxes
    // Get a client using connectToServer
    public async listMailboxes(): Promise<IMailbox[]> {
        const client: any = await this.connectToServer();
        // Grab the mailboxes using a client method (not this function, but same name)
        const mailboxes: any = await client.listMailboxes();
        await client.close();
        const finalMailboxes: IMailbox[] = [];
        // Recursive call to grab all the children 
        const iterateChildren: Function = (inArray: any[]): void => {
            inArray.forEach((inValue: any) => {
                finalMailboxes.push({
                    name : inValue.name, path : inValue.path
                });
                iterateChildren(inValue.children);
            });
        };
        iterateChildren(mailboxes.children);
        return finalMailboxes;
    }

    public async listMessages(inCallOptions: ICallOptions): Promise<IMessage[]> {

        console.log("IMAP.Worker.listMessages()", inCallOptions);
    
        const client: any = await this.connectToServer();
    
        // We have to select the mailbox first.  This gives us the message count.
        const mailbox: any = await client.selectMailbox(inCallOptions.mailbox);
        console.log(`IMAP.Worker.listMessages(): Message count = ${mailbox.exists}`);
    
        // If there are no messages then just return an empty array.
        if (mailbox.exists === 0) {
          await client.close();
          return [ ];
        }
    
        // Okay, there are messages, let's get 'em!  Note that they are returned in order by uid, so it's FIFO.
        // inCallOptions contains name of mailbox in the .mailbox field
        const messages: any[] = await client.listMessages(
          // Grab msgs beginning with first one and all others after it
          // Just grabbing the unique id (uid) and metadata (envelope). Not grabbing msg body.
          inCallOptions.mailbox,
          "1:*",
          [ "uid", "envelope" ]
        );
    
        await client.close();
    
        // Translate from emailjs-imap-client message objects to app-specific objects.
        const finalMessages: IMessage[] = [];
        messages.forEach((inValue: any) => {
          finalMessages.push({
            id : inValue.uid,
            date: inValue.envelope.date,
            from: inValue.envelope.from[0].address,
            subject: inValue.envelope.subject
          });
        });
    
        return finalMessages;
    
    } /* End listMessages(). */


    // Grab the actual body of a message
    public async getMessageBody(inCallOptions: ICallOptions): Promise<string | undefined>  {
        console.log("IMAP.Worker.getMessageBody()", inCallOptions);
        const client: any = await this.connectToServer();
        // Call list messages again, but this time specify we want the body
        const messages: any[] = await client.listMessages(
            // Specifying message we want by its id, so we have to add a fourth param
            inCallOptions.mailbox, inCallOptions.id,
            [ "body[]" ], { byUid : true }
        );
        // Does some processing on the grabbed messages
        const parsed: ParsedMail = await simpleParser(messages[0]["body[]"]);
        await client.close();
        return parsed.text;
    }

    // Telling client we want to delete a message by id.
    public async deleteMessage(inCallOptions: ICallOptions): Promise<any> {
        const client: any = await this.connectToServer();
        await client.deleteMessages(
            inCallOptions.mailbox, inCallOptions.id, { byUid : true }
        );
        await client.close();
    }
}