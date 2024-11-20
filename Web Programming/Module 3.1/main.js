function _defineProperty(e, r, t) { return (r = _toPropertyKey(r)) in e ? Object.defineProperty(e, r, { value: t, enumerable: !0, configurable: !0, writable: !0 }) : e[r] = t, e; }
function _toPropertyKey(t) { var i = _toPrimitive(t, "string"); return "symbol" == typeof i ? i : i + ""; }
function _toPrimitive(t, r) { if ("object" != typeof t || !t) return t; var e = t[Symbol.toPrimitive]; if (void 0 !== e) { var i = e.call(t, r || "default"); if ("object" != typeof i) return i; throw new TypeError("@@toPrimitive must return a primitive value."); } return ("string" === r ? String : Number)(t); }
function start() {
  class Bookmark extends React.Component {
    constructor(props) {
      // When calling constructor for a react component instance, must call superclass's constructor
      // must also pass it props
      super(props);
      // title treated as a member variable and init using the props passed in
      // variable becomes state of object.
      _defineProperty(this, "title", this.props.title);
      _defineProperty(this, "titleStyle", {
        color: "red"
      });
      console.log("Bookmark component created");
    }
    // Prop types necessary to enforce that a prop must be a certain type
    // propTypes is like defaultProps in that it's a property of the component class
    // If passing an incorrect type, React outputs a message
    // Enforces that description should have a number, and throws an error.

    render() {
      return /*#__PURE__*/React.createElement("li", null, /*#__PURE__*/React.createElement("h2", {
        style: this.titleStyle
      }, this.title), /*#__PURE__*/React.createElement("a", {
        href: this.props.href
      }, this.props.description), /*#__PURE__*/React.createElement("button", {
        onClick: () => {
          this.title = this.title + "-CHANGED";
          this.setState({});
        }
      }, "Click me"));
    }
  }
  _defineProperty(Bookmark, "propTypes", {
    description: PropTypes.number
  });
  // Default props property -- feature of the component class
  // Define default values for props
  // Good idea to define default props.
  _defineProperty(Bookmark, "defaultProps", {
    description: "Unknown"
  });
  ReactDOM.render(/*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h1", null, "Bookmarks"), /*#__PURE__*/React.createElement("ul", null, /*#__PURE__*/React.createElement(Bookmark, {
    title: "Etherient",
    href: "https://www.etherient.com",
    description: "The home page of Etherient"
  }), /*#__PURE__*/React.createElement(Bookmark, {
    title: "Frank's Site",
    href: "https://www.zammetti.com",
    description: "The web home of Frank W. Zammetti"
  }))), document.getElementById("mainContainer"));
}

start()