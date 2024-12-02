import React from "react";

// Define shape of component's state using TS
interface DuckState {
  duckCount: number;
  duckString: string;
}
// Class component manages DuckState
// No props expected, but it is passed a state type to enforce the type of the state (as defined by the interface)
class DuckComponent extends React.Component<{}, DuckState> {
  constructor(props: {}) {
    super(props);
    // Set initial state
    this.state = {
      duckCount: 0,
      duckString: "", // Where duck emojis will be added
    };
  }
  // Arrow function to add a duck
  addDuck = () => {
    this.setState((prevState) => ({
      duckCount: prevState.duckCount + 1,
      duckString: prevState.duckString + "🦆",
    }));
    this.quack();

  };

  // Arrow function to make the duck sound
  quack = () => {
    alert("Quack");
  };

  // Required render method to define what component looks like
  render() {
    return (
      <div>
        <h2>{this.state.duckString}</h2>
        <button onClick={this.addDuck}>Add Duck</button>
      </div>
    );
  }
}

export default DuckComponent;