// import getA from "./module1";
// import getB from "./module2";
// alert(getA() * getB());


// index.ts
// Find the h2 element
const tipElement = document.querySelector('h2');

if (tipElement) {
    // Change its text
    tipElement.textContent = "This text was changed by TypeScript!";
    
    // Change its style
    tipElement.style.color = 'blue';
    
    // Add a click handler
    tipElement.addEventListener('click', () => {
        alert('You clicked the tip!');
    });
}