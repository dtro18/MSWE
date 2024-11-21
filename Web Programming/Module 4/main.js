function _defineProperty(e, r, t) { return (r = _toPropertyKey(r)) in e ? Object.defineProperty(e, r, { value: t, enumerable: !0, configurable: !0, writable: !0 }) : e[r] = t, e; }
function _toPropertyKey(t) { var i = _toPrimitive(t, "string"); return "symbol" == typeof i ? i : i + ""; }
function _toPrimitive(t, r) { if ("object" != typeof t || !t) return t; var e = t[Symbol.toPrimitive]; if (void 0 !== e) { var i = e.call(t, r || "default"); if ("object" != typeof i) return i; throw new TypeError("@@toPrimitive must return a primitive value."); } return ("string" === r ? String : Number)(t); }
function start() {
  class Course extends React.Component {
    constructor(props) {
      // When calling constructor for a react component instance, must call superclass's constructor
      // must also pass it props
      super(props);
      // title treated as a member variable and init using the props passed in
      // variable becomes state of object.
      _defineProperty(this, "courseTitle", this.props.title);
    }
    // Prop types necessary to enforce that a prop must be a certain type
    // propTypes is like defaultProps in that it's a property of the component class
    // If passing an incorrect type, React outputs a message
    // Enforces that description should have a number, and throws an error.

    render() {
      return /*#__PURE__*/React.createElement("li", null, /*#__PURE__*/React.createElement("h2", null, this.courseTitle), /*#__PURE__*/React.createElement("h3", null, "Course Grade: " + this.props.courseGrade), /*#__PURE__*/React.createElement("button", {
        onClick: () => {
          this.courseTitle = this.courseTitle + " - PASSED";
          this.setState({});
        }
      }, "Marked Completed"));
    }
  }
  _defineProperty(Course, "propTypes", {
    description: PropTypes.string
  });
  // Default props property -- feature of the component class
  // Define default values for props
  // Good idea to define default props.
  _defineProperty(Course, "defaultProps", {
    courseGrade: "N/A"
  });
  ReactDOM.render(/*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h1", null, "Fall 2024 Schedule"), /*#__PURE__*/React.createElement("ul", null, /*#__PURE__*/React.createElement(Course, {
    title: "240 Data Structures",
    courseGrade: 99.87
  }), /*#__PURE__*/React.createElement(Course, {
    title: "241 Algorithms",
    courseGrade: 99.85
  }), /*#__PURE__*/React.createElement(Course, {
    title: "242 Network Programming",
    courseGrade: 99.87
  }), /*#__PURE__*/React.createElement(Course, {
    title: "243 Database Programming",
    courseGrade: 99.85
  }), /*#__PURE__*/React.createElement(Course, {
    title: "244 Concurrent Programming"
  }), /*#__PURE__*/React.createElement(Course, {
    title: "250 Web Programming"
  }))), document.getElementById("mainContainer"));
}
start();
