import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import NavBar from "./components/navbar";
import Counters from "./components/counters";

class App extends Component {
  state = {
    counters: [
      { id: 0, value: 0, title: "產品1" },
      { id: 1, value: 0, title: "產品2" },
      { id: 2, value: 0, title: "產品3" },
      { id: 3, value: 0, title: "產品4" }
    ]
  };

  constructor() {
    super();
    console.log("[App] constructor");
  }

  onIncrement = counter => {
    console.log("[App] onIncrement !", counter);
    const counters = [...this.state.counters];
    //const curCounter = counters.filter(c => c.id === counter.id)[0];
    const index = counters.indexOf(counter);
    const curCounter = counters[index];
    curCounter.value = counter.value;
    this.setState({ counters });
  };

  onDelete = counterId => {
    console.log("[App] onDelete !", counterId);
    const counters = this.state.counters.filter(c => c.id !== counterId);
    this.setState({ counters: counters });
  };

  onReset = () => {
    console.log("[App] onReset !");
    const counters = this.state.counters.map(c => {
      c.value = 0;
      return c;
    });
    this.setState({ counters });
  };

  caculateTotalCounters = () => {
    const totalCounters = this.state.counters.filter(c => c.value > 0).length;
    console.log("totalCounters : ", totalCounters);
    return totalCounters;
  };

  render() {
    console.log("[App] Rendered");
    return (
      <React.Fragment>
        <NavBar totalCounters={this.caculateTotalCounters()} />
        <main className="container">
          <Counters
            onIncrement={this.onIncrement}
            onDelete={this.onDelete}
            onReset={this.onReset}
            counters={this.state.counters}
          />
        </main>
      </React.Fragment>
    );
  }

  //-------------------------------------------------------
  componentDidMount() {
    console.log("[App] Mounted");
  }
  componentDidUpdate(prevProps, prevState) {
    console.log("[App] Updated");
    console.log("prevProps", prevProps);
    console.log("prevState", prevState);
  }
  componentWillUnmount() {
    console.log("[App] Unmounted");
  }
}

export default App;
