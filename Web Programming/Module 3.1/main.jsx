function start() {
  class Course extends React.Component {
    constructor(props) {
      // When calling constructor for a react component instance, must call superclass's constructor
      // must also pass it props
      super(props);
    }
    // Prop types necessary to enforce that a prop must be a certain type
    // propTypes is like defaultProps in that it's a property of the component class
    // If passing an incorrect type, React outputs a message
    // Enforces that description should have a number, and throws an error.
    static propTypes = { description : PropTypes.string}
    // Default props property -- feature of the component class
    // Define default values for props
    // Good idea to define default props.
    static defaultProps = { courseGrade: "N/A" }
    // title treated as a member variable and init using the props passed in
    // variable becomes state of object.
    courseTitle = this.props.title;

    render() {
      return (
        <li>
          <h2>{this.courseTitle}</h2>
          <h3>{"Course Grade: " + this.props.courseGrade}</h3>
          <button
            onClick={() => {
              this.courseTitle = this.courseTitle + " - PASSED";
              this.setState({});
            }}
          >
            Marked Completed
          </button>
        </li>
      );
    }
  }


  ReactDOM.render(
    <div>
      <h1>Fall 2024 Schedule</h1>
      <ul>
        <Course
          title={"240 Data Structures"}
          courseGrade={99.87}
        />
        <Course
          title={"241 Algorithms"}
          courseGrade={99.85}
        />
        <Course
          title={"242 Network Programming"}
          courseGrade={99.87}
        />
        <Course
          title={"243 Database Programming"}
          courseGrade={99.85}
        />
      </ul>
    </div>,
    document.getElementById("mainContainer")
  );
}

start();