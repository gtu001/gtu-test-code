import React, { Component } from "react";

class Counter extends Component {
  state = {
    counter : this.props.counter,
    tag: [] //["tag1", "tag2", "tag3"]
  };

  constructor(props) {
    super(props);
    console.log("constructor Prop : ", this.props);
    this.handleIncrement = this.handleIncrement.bind(this);
  }

  styles = {
    fontSize: 10,
    fontWeight: "bold"
  };

  renderTag() {
    if (this.state.tag.length === 0) {
      return <p>There is no tag</p>;
    } else {
      return this.state.tag.map(tag => (
        <ul>
          <li key={tag}>{tag}</li>
        </ul>
      ));
    }
  }

  handleIncrement(product) {
    //要在constructor 做 bind
    // 這個做法無法由 parent 取得 子元素狀態
    console.log("Increment clicked! ", this.state);
    console.log("handleIncrement ", product);
    product.value ++;
    this.setState({ counter : product });

    // 所以還要呼叫parent去update
    this.props.onIncrement(this.props.counter);
  }

  handleIncrement2 = product => {
    console.log("Increment clicked! ", this.state);
    console.log("handleIncrement2 ", product);
    product.value ++;
    this.setState({ counter : product });
  };

  handleDelete = () => {
    console.log("handleDelete clicked! ", this.state);
    this.props.onDelete(this.state.counter.id); //呼叫parent event
  };

  render() {
    console.log("[Counter] Rendered");
    return (
      <React.Fragment>
        {this.props.children}

        <span style={this.styles} className={this.getBadgeClasses()}>
          {this.formatValue()}
        </span>
        <button
          onClick={() => this.handleIncrement(this.state.counter)}
          className="btn btn-secondary btn-sm"
        >
          Increment
        </button>

        <button
          onClick={this.handleDelete}
          className="btn btn-danger btn-sm m-2"
        >
          Delete
        </button>

        {this.state.tag.length === 0 && "Please add the tags!"}
        {this.renderTag()}
      </React.Fragment>
    );
  }

  getBadgeClasses() {
    let classes = "badge m-2 badge-";
    classes += this.state.counter.value === 0 ? "warning" : "primary";
    return classes;
  }

  formatValue() {
    const { value } = this.state.counter;
    return value === 0 ? "Zero" : value;
  }

  //----------------------------------------------------------------------
  componentDidUpdate(prevProps, prevState) {
    console.log("[Counter] Updated");
    console.log("prevProps", prevProps);
    console.log("prevState", prevState);
  }

  componentWillUnmount() {
    console.log("[Counter] Unmounted");
  }
}

export default Counter;
