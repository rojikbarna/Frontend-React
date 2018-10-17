import React from 'react';
import {Jumbotron} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import MyHeader from './../header/header';

export default class Logout extends React.Component {

  render () {
    return (
      <div>
        <MyHeader />
        <Jumbotron>
          <h1>Bye </h1>
          <h2> you can login with click <Link to='/'>here</Link> </h2>
        </Jumbotron>
      </div>
    );
  }
}
