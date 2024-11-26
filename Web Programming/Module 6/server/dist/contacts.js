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
const path = __importStar(require("path"));
const Datastore = require("nedb");
class Worker {
    constructor() {
        this.db = new Datastore({
            // db is supplied path to contacts db file, or creates it here if it doesn't exist
            filename: path.join(__dirname, "contacts.db"),
            autoload: true
        });
    }
    // Grab list of contacts from Db
    listContacts() {
        return new Promise((inResolve, inReject) => {
            // Call db find and specify no search criteria as the first argument
            this.db.find({}, (inError, inDocs) => {
                // Reject promise if error happens
                if (inError) {
                    inReject(inError);
                    // Otherwise, return the array of documents 
                }
                else {
                    inResolve(inDocs);
                }
            });
        });
    }
    // Add a new contact to the db
    addContact(inContact) {
        return new Promise((inResolve, inReject) => {
            // Pass contact to be added as first param
            this.db.insert(inContact, 
            // Callback function, to be executed after .insert attempts to add a contact into the database
            (inError, inNewDoc) => {
                if (inError) {
                    inReject(inError);
                }
                else {
                    inResolve(inNewDoc);
                }
            });
        });
    }
    // Delete a contact from the db
    deleteContact(inID) {
        console.log("Contacts.Worker.deleteContact()", inID);
        return new Promise((inResolve, inReject) => {
            // Must provide a query, the ID we want to delete.
            this.db.remove(
            // The query object itself
            { _id: inID }, {}, 
            // Callback function to be called after attempting deletion
            // Callback passed # of documents remove.
            (inError, inNumRemoved) => {
                if (inError) {
                    console.log("Contacts.Worker.deleteContact(): Error", inError);
                    inReject(inError);
                }
                else {
                    console.log("Contacts.Worker.deleteContact(): Ok", inNumRemoved);
                    inResolve("");
                }
            });
        });
    } /* End deleteContact(). */
}
exports.Worker = Worker;
//# sourceMappingURL=Contacts.js.map