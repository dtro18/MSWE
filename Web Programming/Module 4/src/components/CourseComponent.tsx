import React from "react";

interface CourseProps {
  title: string;
  courseGrade?: number | string;
}

class Course extends React.Component<CourseProps> {
  constructor(props: CourseProps) {
    super(props);
  }

  // Default value for courseGrade if not provided
  static defaultProps: Partial<CourseProps> = {
    courseGrade: "N/A",
  };

  // State is not needed for this component, as it only modifies the title
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

export default Course;