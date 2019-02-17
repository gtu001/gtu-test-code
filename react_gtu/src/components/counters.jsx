import React, { Component } from "react";
import Counter from "./counter";

class Counters extends Component {
  state = {
    counters: this.props.counters
  };

  constructor(props) {
    super(props);
  }

  handleIncrement = counter => {
    console.log("handleIncrement Event Called!", counter);
    const counters = [...this.state.counters];
    //const curCounter = counters.filter(c => c.id === counter.id)[0];
    const index = counters.indexOf(counter);
    const curCounter = counters[index];
    curCounter.value = counter.value;
    this.setState({ counters });

    // 呼叫parent
    this.props.onIncrement(counter);
  };

  handleDelete = counterId => {
    console.log("handleDelete Event Called!", counterId);
    const counters = this.state.counters.filter(c => c.id !== counterId);
    this.setState({ counters: counters });

    // 呼叫parent
    this.props.onDelete(counterId);
  };

  handleReset = () => {
    const counters = this.state.counters.map(c => {
      c.value = 0;
      return c;
    });
    this.setState({ counters });

    // 呼叫parent
    this.props.onReset();
  };

  render() {
    console.log("[Counters] Rendered");
    return (
      <div>
        <button
          onClick={this.handleReset}
          className="btn btn-primary btn-sm m-2"
        >
          Reset
        </button>

        {this.state.counters.map(counter => (
          <Counter
            key={counter.id}
            onDelete={this.handleDelete}
            onIncrement={this.handleIncrement}
            counter={counter}
          >
            <h4>{counter.title}</h4>
          </Counter>
        ))}
      </div>
    );
  }
}

export default Counters;
