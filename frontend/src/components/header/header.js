import React from 'react';
import {NavLink} from 'react-router-dom';
export default class MyHeader extends React.Component {
  render () {
    return (
      <div>
        <header>
          <NavLink to='/products' style={{
    fontWeight: 'bold',
    color: 'white'
   }}> Products</NavLink>
          <NavLink to='/achievements' style={{
    fontWeight: 'bold',
    color: '#white'
   }}> Achivements</NavLink>
          <NavLink to='/orders' style={{
    fontWeight: 'bold',
    color: '#white'
   }}>Orders</NavLink>
          <NavLink to='/users' style={{
    fontWeight: 'bold',
    color: '#white'
   }}>Users</NavLink>
          <NavLink to='/logout' style={{
    fontWeight: 'bold',
    color: '#white'
   }}>Logout</NavLink>
        </header>      </div>
    );
  }
}
