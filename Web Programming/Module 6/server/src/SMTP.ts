// Nodemailer module allows Node applications to send emails
// Create a "transport" object which talks to a mailing protocol
// calling sendMail() on the transport object just...sends it
import Mail from "nodemailer/lib/mailer";
import * as nodemailer from "nodemailer";
import { SendMailOptions, SentMessageInfo } from "nodemailer";

// App imports.
import { IServerInfo } from "./ServerInfo";


export class Worker {
    private static serverInfo: IServerInfo;

    constructor(inServerInfo: IServerInfo) {
    Worker.serverInfo = inServerInfo;
    }

    // Sendmessage method takes in an object that must adhere to SendMailOptions interfaced, defined by module
    // Promise is to return a string, eventually
    public sendMessage(inOptions: SendMailOptions): Promise<string> {

        console.log("SMTP.Worker.sendMessage()", inOptions);
        // Callback APIs are wrapped in promises
        // Promise is returned, and the caller (using async/await) can access it.
        return new Promise((inResolve, inReject) => {
          // Get connection to the SMTP server
          const transport: Mail = nodemailer.createTransport(Worker.serverInfo.smtp);
          // inOptions is the message details
          transport.sendMail(
            inOptions,
            // Callback function is second param to sendmail

            (inError: Error | null, inInfo: SentMessageInfo) => {
              if (inError) {
                console.log("SMTP.Worker.sendMessage(): Error", inError);
                inReject(inError);
              } else {
                console.log("SMTP.Worker.sendMessage(): Ok", inInfo);
                inResolve("");
              }
            }
          );
        });
    
      } /* End sendMessage(). */
}
