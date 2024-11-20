function start() {
    class Bookmark extends React.Component {
      constructor(props) {
        // When calling constructor for a react component instance, must call superclass's constructor
        // must also pass it props
        super(props);
        console.log("Bookmark component created");
      }
      // Prop types necessary to enforce that a prop must be a certain type
      // propTypes is like defaultProps in that it's a property of the component class
      // If passing an incorrect type, React outputs a message
      // Enforces that description should have a number, and throws an error.
      static propTypes = { description : PropTypes.string}
      // Default props property -- feature of the component class
      // Define default values for props
      // Good idea to define default props.
      static defaultProps = { description: "Unknown" }
      // title treated as a member variable and init using the props passed in
      // variable becomes state of object.
      title = this.props.title;
      titleStyle = { color: "red" };
  
      render() {
        return (
          <li>
            <h2 style={this.titleStyle}>{this.title}</h2>
            <a href={this.props.href}>{this.props.description}</a>
            <button
              onClick={() => {
                this.title = this.title + "-CHANGED";
                this.setState({});
              }}
            >
              Click me
            </button>
          </li>
        );
      }
    }
  
    ReactDOM.render(
      <div>
        <h1>Bookmarks</h1>
        <ul>
          <Bookmark
            title={"Etherient"}
            href={"https://www.etherient.com"}
            description={"The home page of Etherient"}
          />
          <Bookmark
            title={"Frank's Site"}
            href={"https://www.zammetti.com"}
            description={"The web home of Frank W. Zammetti"}
          />
        </ul>
      </div>,
      document.getElementById("mainContainer")
    );
  }
  