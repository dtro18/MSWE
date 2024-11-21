import React from "react";
interface CourseProps {
  title: string;
  courseGrade?: number | string;
}

function start() {
  class Course extends React.Component<CourseProps> {
    constructor(props: CourseProps) {
      // When calling constructor for a react component instance, must call superclass's constructor
      // must also pass it props
      super(props);
    }
    // Partial - don't need to provide every param
    // Provides a default value for courseGrade if it is not supplied
    static defaultProps: Partial<CourseProps> = {
      courseGrade: "N/A",
    };
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
        <Course
          title={"244 Concurrent Programming"}
        />
        <Course
          title={"250 Web Programming"}
        />
      </ul>
    </div>,
    document.getElementById("mainContainer")
  );
}

start();