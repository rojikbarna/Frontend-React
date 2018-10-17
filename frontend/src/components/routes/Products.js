import React from 'react';
import axios from 'axios';
import MyHeader from './../header/header';

export default class Products extends React.Component {
  constructor (props) {
    super(props);
    this.state = {
      items: []
    };
  }

  componentDidMount () {
    var config = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Authorization': document.cookie,
      crossdomain: true
    };

    axios
      .get('http://127.0.0.1:8080/products', { headers: config })
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

  goto () {
    window.location.replace('/newProduct');
  }

  render () {
    console.log(this.state);
    return (
      <div>
        <MyHeader />
        <h3>Products</h3>
        <button onClick={this.goto.bind(this)} type='submit'>Add new product</button>
        <ul>List of all the products: {this.renderProducts()}</ul>
      </div>
    );
  }

  renderProducts () {
    console.log(this.state.items);
    const renderProducts = this.state.items.map(function (product, i) {
      return <li key={product.id}>Category: {product.category}, Name: {product.name}, Price: {product.price}, xpValue: {product.xpValue}
      </li>;
    });

    return renderProducts;
  }
}
