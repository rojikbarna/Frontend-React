import React from 'react';
import axios from 'axios';
import MyHeader from './../header/header';
import {NavLink} from 'react-router-dom';

export default class AchievementDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: [],
      id: this.props.id
    };
  }

  componentWillMount() {
    var config = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Authorization': document.cookie,
      crossdomain: true
    };
    console.log(this.props.id);
    console.log(this.state.id);
    axios
      .get(`http://127.0.0.1:8080/achievements/${this.state.id}/achievement_conditions`, { headers: config })
      .then(({ data }) => {
        console.log(data);
        this.setState(
          { items: data }
        );
      })
      .catch(function (error) {
        if (error.response) {
          console.log(error.response.data);
          console.log(error.response.status);
          console.log(error.response.headers);
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log('Error', error.message);
        }
        console.log(error.config);
        console.log(error);
      });
  }

  render() {
    console.log(this.state);
    return (
      <div>
        <ul>List of conditions: {this.renderConditions()}</ul>
      </div>
    );
  }

  renderConditions() {
    console.log(this.state.items);
    const renderConditions = this.state.items.map(function (condition, i) {
      return <li key={condition.id}> Quantity: {condition.quantity}, Product: {condition.product.name} </li>;
    });
    return renderConditions;
  }
}
