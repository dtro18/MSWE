// Types are declared with the :<type> syntax
// Can be used in variable declaration, or to say what type a function returns
// String type.
const bestShowEver: string = "Babylon 5";
// Number type.
const a: number = 5
const b: number = 5.55
// Any type
let food: any = "pizza"
// Arrays
// const pets = [ "Belle", "Bubbles"]; // Type will be inferred from initialization values
// const pets: string[] = [ "Belle", "Bubbles"]

food = 113; // is ok
// Assigns things numbers
enum Food { Pizza, FriedChicken = 500, IceCream };
let myFavoriteFood = Food.FriedChicken;
function saysFood() {
    alert(myFavoriteFood);

}
saysFood();

// Contract: must take two nums and return a string
let myMathFunction: (num1: number, num2: number) => string;
// So this is legal
function add(n1: number, n2: number): string {
 return "" + n1 + n2;
}
myMathFunction = add;

// But this is not legal:
function multiply(a: number, b: number): number {
    return a * b;
   }
// myMathFunction = multiply;

let myAge: number | string;
myAge = 46
myAge = "25"
// myAge = true; fails because it's not in the allowed types

// Define a type that TS will enforce
type PersonType = {
    firstName: string,
    lastName: string,
    age: number
}

let person1: PersonType = {
    firstName: "Dylan",
    lastName: "Tran",
    age: 42
};

// Arrow functions:
// No need to type "function" or specify a return value
// const test = (name) => {
//     alert(`Hello, ${name}`);
// }
// test("Jack");

const addNums = (a: number, b: number): number => a + b;
alert(addNums(2, 3));


// TS has built in getter/setter functions

class Planet {
    private _name: string = "No name set";
    get name() {
        return `This planet's name is '${this._name}'.`;
    }

    set name(inName: string) {
        if (inName === "Pluto") {
            this._name = "Not a planet";
        } else {
            this._name = inName;
        }
    }
}
let p: Planet = new Planet();
// Calling p.name is the same as p.name(), which will direct to the getter
alert(p.name); // 'No name set'.
// Calling p.name in this context will direct to the setter.    
p.name = "Pluto";
alert(p.name); // 'Not a planet' (sorry, little guy!)
p.name = "Venus";
alert(p.name); // 'Venus'