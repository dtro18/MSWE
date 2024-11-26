import * as path from "path";
const Datastore = require("nedb");

// Grab path to data file
// Adding a contact, id won't be provided by client. It will be provided by NeDB
export interface IContact {
    _id?: number, name: string, email: string
}
   
export class Worker {
    // Nedb datastore instance for contacts
    private db: Nedb;
    constructor() {
        this.db = new Datastore({
            // db is supplied path to contacts db file, or creates it here if it doesn't exist
            filename : path.join(__dirname, "contacts.db"),
            autoload : true
        });
    }
    // Grab list of contacts from Db
    public listContacts(): Promise<IContact[]> {
        return new Promise((inResolve, inReject) => {
            // Call db find and specify no search criteria as the first argument

            this.db.find({ },
                (inError: Error, inDocs: IContact[]) => {
                    // Reject promise if error happens
                    if (inError) {
                        inReject(inError);
                    // Otherwise, return the array of documents 
                    } else {
                        inResolve(inDocs);
                    }
                }
            );
        });
    }

    // Add a new contact to the db
    public addContact(inContact: IContact): Promise<IContact> {
        return new Promise((inResolve, inReject) => {
            // Pass contact to be added as first param
            this.db.insert(
                inContact,
                // Callback function, to be executed after .insert attempts to add a contact into the database
                (inError: Error | null, inNewDoc: IContact) => {
                    if (inError) {
                        inReject(inError);
                    } else {
                        inResolve(inNewDoc);
                    }
                }
            );
        });
    }

    // Delete a contact from the db
    public deleteContact(inID: string): Promise<string> {

        console.log("Contacts.Worker.deleteContact()", inID);
    
        return new Promise((inResolve, inReject) => {
          // Must provide a query, the ID we want to delete.
          this.db.remove(
            // The query object itself
            { _id : inID },
            { },
            // Callback function to be called after attempting deletion
            // Callback passed # of documents remove.
            (inError: Error | null, inNumRemoved: number) => {
              if (inError) {
                console.log("Contacts.Worker.deleteContact(): Error", inError);
                inReject(inError);
              } else {
                console.log("Contacts.Worker.deleteContact(): Ok", inNumRemoved);
                inResolve("");
              }
            }
          );
        });
    
    } /* End deleteContact(). */
}