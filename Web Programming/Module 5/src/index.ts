import { User, generateUser} from "./module1";

// Run server by npm run start
// index.ts
// Find the button element
const button = document.getElementById('createPersonButton');
// Find the text box to be changed
const tipElement = document.querySelector('h2');

if (button) {
    button.addEventListener('click', () => {
        const person = generateUser();
        if (tipElement) {
            tipElement.innerHTML = `
            ID: ${person.id}<br>
            Name: ${person.name}<br>
            Email: ${person.email}<br>
            Birthday: ${person.birthday}<br>
            Occupation: ${person.occupation}
        `;
        }
        
    });
}