var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
import { jsx as _jsx, jsxs as _jsxs } from "react/jsx-runtime";
import React from "react";
import ReactDOM from 'react-dom/client';
var DuckComponent = /** @class */ (function (_super) {
    __extends(DuckComponent, _super);
    function DuckComponent(props) {
        var _this = _super.call(this, props) || this;
        _this.addDuck = function () {
            _this.setState(function (prevState) { return ({
                duckCount: prevState.duckCount + 1,
                duckString: prevState.duckString + "🦆",
            }); });
            _this.quack();
        };
        _this.quack = function () {
            alert("Quack");
        };
        _this.state = {
            duckCount: 0,
            duckString: "" // Where duck emojis will be added
        };
        return _this;
    }
    DuckComponent.prototype.render = function () {
        return (_jsxs("div", { children: [_jsx("h2", { children: this.state.duckString }), _jsx("button", { onClick: this.addDuck, children: " Add Duck" })] }));
    };
    return DuckComponent;
}(React.Component));
var Course = /** @class */ (function (_super) {
    __extends(Course, _super);
    function Course(props) {
        var _this = _super.call(this, props) || this;
        // title treated as a member variable and init using the props passed in
        // variable becomes state of object.
        _this.courseTitle = _this.props.title;
        return _this;
    }
    Course.prototype.render = function () {
        var _this = this;
        return (_jsxs("li", { children: [_jsx("h2", { children: this.courseTitle }), _jsx("h3", { children: "Course Grade: " + this.props.courseGrade }), _jsx("button", { onClick: function () {
                        _this.courseTitle = _this.courseTitle + " - PASSED";
                        _this.setState({});
                    }, children: "Marked Completed" })] }));
    };
    // Partial - don't need to provide every param
    // Provides a default value for courseGrade if it is not supplied
    Course.defaultProps = {
        courseGrade: "N/A",
    };
    return Course;
}(React.Component));
var root = ReactDOM.createRoot(document.getElementById('mainContainer'));
function start() {
    root.render(_jsxs("div", { children: [_jsx("h1", { children: "Duck Counter" }), _jsx(DuckComponent, {})] }));
}
start();
//# sourceMappingURL=main.js.map