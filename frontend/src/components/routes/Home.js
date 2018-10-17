import React from 'react';
import {Jumbotron} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import MyHeader from './../header/header';

export default class Home extends React.Component {
  render () {
    return (
      <div>
        <MyHeader />
        <Jumbotron>
          <h1>Welcome </h1>
          <h2> Admin home page </h2>
          <Link to='/registration'>New admin registration</Link>
        </Jumbotron>
      </div>
    );
  }
}
