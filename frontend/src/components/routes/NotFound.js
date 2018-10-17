import React from 'react';
import { Jumbotron } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export default class NotFound extends React.Component {
  render () {
    return (
      <div>
        <Jumbotron>
          <h1>Ooooopsss</h1>
          <h2>you're drunk, go <Link to='/home'>home</Link> </h2>
        </Jumbotron>
      </div>
    );
  }
}
