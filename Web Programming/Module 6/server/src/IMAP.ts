
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