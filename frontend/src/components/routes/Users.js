import React from 'react';
import axios from 'axios';
import MyHeader from './../header/header';

export default class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: []
    };
  }

  componentDidMount() {
    var config = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Authorization': document.cookie,
      crossdomain: true
    };

    axios
      .get('http://127.0.0.1:8080/users', { headers: config })
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

  render () {
    console.log(this.state);
    return (
      <div>
        <MyHeader />
        <h3>Users</h3>
        <ul>List of all the users: {this.renderUsers()}</ul>
      </div>
    );
  }

  renderUsers () {
    console.log(this.state.items);
    const renderUser = this.state.items.map(function (user, i) {
      return <li key={user.id}>XP: {user.xp}, Nickame: {user.nickName} (Real name: {user.firstName} {user.lastName})
      </li>;
    });

    return renderUser;
  }
}
