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
