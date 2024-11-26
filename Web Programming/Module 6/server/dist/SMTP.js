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
Object.defineProperty(exports, "__esModule", { value: true });
exports.Worker = void 0;
const nodemailer = __importStar(require("nodemailer"));
class Worker {
    constructor(inServerInfo) {
        Worker.serverInfo = inServerInfo;
    }
    // Sendmessage method takes in an object that must adhere to SendMailOptions interfaced, defined by module
    // Promise is to return a string, eventually
    sendMessage(inOptions) {
        console.log("SMTP.Worker.sendMessage()", inOptions);
        // Callback APIs are wrapped in promises
        // Promise is returned, and the caller (using async/await) can access it.
        return new Promise((inResolve, inReject) => {
            // Get connection to the SMTP server
            const transport = nodemailer.createTransport(Worker.serverInfo.smtp);
            // inOptions is the message details
            transport.sendMail(inOptions, 
            // Callback function is second param to sendmail
            (inError, inInfo) => {
                if (inError) {
                    console.log("SMTP.Worker.sendMessage(): Error", inError);
                    inReject(inError);
                }
                else {
                    console.log("SMTP.Worker.sendMessage(): Ok", inInfo);
                    inResolve("");
                }
            });
        });
    } /* End sendMessage(). */
}
exports.Worker = Worker;
//# sourceMappingURL=SMTP.js.map