import React from 'react';
import axios from 'axios';
import MyHeader from './../header/header';

export default class Orders extends React.Component {
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
      .get('http://127.0.0.1:8080/ordering', { headers: config })
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

  goto = (event2) => {
    event2.preventDefault();
    var config2 = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Authorization': document.cookie,
      crossdomain: true
    };
    axios.defaults.headers.post['Content-Type'] = 'application/json';
    axios
      .post('http://127.0.0.1:8080/ordering',  { headers: config2 })
      .then(function (response) {
        const status = JSON.parse(response.status);
        const orderID = JSON.parse(response.data.id);
        window.location.replace(`/ordering/${orderID}/newOrder`);
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
    return (
      <div>
        <MyHeader />
        <h3>Orders</h3>
        <button onClick={this.goto.bind(this)} type='submit'>New order</button>
        <ul>List of all the orders: {this.renderOrders()}</ul>
      </div>
    );
  }

  viewQrCode (filename) {
    window.open('localhost:3000/' + filename, "_blank");
  }

  renderOrders () {
    console.log(this.state.items);
    const renderOrders = this.state.items.map((order, i) => {
      return (
        <form >
          <li key={order.id}>Created at: {new Intl.DateTimeFormat('hu-HU', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
}).format(order.created)} <button onClick={this.viewQrCode.bind(this, order.qrCodePath)} type='submit'>view QR code</button>
          </li>
        </form>
      );
    });
    return renderOrders;
  }
}
