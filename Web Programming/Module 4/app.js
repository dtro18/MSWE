"use strict";
// Types are declared with the :<type> syntax
// Can be used in variable declaration, or to say what type a function returns
// String type.
var bestShowEver = "Babylon 5";
// Number type.
var a = 5;
var b = 5.55;
// Any type
var food = "pizza";
// Arrays
// const pets = [ "Belle", "Bubbles"]; // Type will be inferred from initialization values
// const pets: string[] = [ "Belle", "Bubbles"]
food = 113; // is ok
// Assigns things numbers
var Food;
(function (Food) {
    Food[Food["Pizza"] = 0] = "Pizza";
    Food[Food["FriedChicken"] = 500] = "FriedChicken";
    Food[Food["IceCream"] = 501] = "IceCream";
})(Food || (Food = {}));
;
var myFavoriteFood = Food.FriedChicken;
function saysFood() {
    alert(myFavoriteFood);
}
saysFood();
// Contract: must take two nums and return a string
var myMathFunction;
// So this is legal
function add(n1, n2) {
    return "" + n1 + n2;
}
myMathFunction = add;
// But this is not legal:
function multiply(a, b) {
    return a * b;
}
// myMathFunction = multiply;
var myAge;
myAge = 46;
myAge = "25";
var person1 = {
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
var addNums = function (a, b) { return a + b; };
alert(addNums(2, 3));
// TS has built in getter/setter functions
var Planet = /** @class */ (function () {
    function Planet() {
        this._name = "No name set";
    }
    Object.defineProperty(Planet.prototype, "name", {
        get: function () {
            return "This planet's name is '".concat(this._name, "'.");
        },
        set: function (inName) {
            if (inName === "Pluto") {
                this._name = "Not a planet";
            }
            else {
                this._name = inName;
            }
        },
        enumerable: false,
        configurable: true
    });
    return Planet;
}());
var p = new Planet();
// Calling p.name is the same as p.name(), which will direct to the getter
alert(p.name); // 'No name set'.
// Calling p.name in this context will direct to the setter.    
p.name = "Pluto";
alert(p.name); // 'Not a planet' (sorry, little guy!)
p.name = "Venus";
alert(p.name); // 'Venus'
//# sourceMappingURL=app.js.map