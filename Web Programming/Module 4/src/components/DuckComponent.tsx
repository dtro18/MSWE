import React from "react";

interface DuckState {
  duckCount: number;
  duckString: string;
}

class DuckComponent extends React.Component<{}, DuckState> {
  constructor(props: {}) {
    super(props);
    this.state = {
      duckCount: 0,
      duckString: "", // Where duck emojis will be added
    };
  }

  addDuck = () => {
    this.setState((prevState) => ({
      duckCount: prevState.duckCount + 1,
      duckString: prevState.duckString + "🦆",
    }));
    this.quack();

  };

  quack = () => {
    alert("Quack");
  };

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