import React from 'react';
import FormFields from './../../widgets/forms/FormFields';
import axios from 'axios';

class CreateAchievementConditionForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      formData: {
        quantity: {
          element: 'input',
          value: '',
          label: true,
          labelText: 'quantity',
          config: {
            name: 'name_input',
            text: 'text',
            placeholder: 'enter quantity'
          },
          validation: {
            required: false,
          },
          valid: true,
        },

        productName: {
          element: 'select',
          value: '',
          label: true,
          labelText: 'product',
          config: {
            name: 'name_input',
            options: [],

          },
          validation: {
            required: false,
          },
          valid: true,
        }
      },
      id: this.props.id,
      productList: []
    };
  }

   async componentWillMount() {
    var config = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Authorization': document.cookie,
      crossdomain: true
    };
    await axios
      .get('http://127.0.0.1:8080/products', { headers: config })
      .then(({ data }) => {
        console.log(data);
        this.setState(
          { productList: data }
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
    console.log(this.props.products);
    var list = [];
    var element = {val: '', text: ''};
    list.push({val: 'Select product', text: 'Select product'})
    this.state.productList.forEach(product => {
      element = {val: '', text: ''};
      element.val = product.name;
      element.text = product.name;
      list.push(element);
    });
    console.log(list);
    this.setState({formData : {
      quantity: {
        element: 'input',
        value: '',
        label: true,
        labelText: 'Quantity',
        config: {
          name: 'name_input',
          text: 'text',
          placeholder: 'enter quantity'
        },
        validation: {
          required: false,
        },
        valid: true,
      },

      productName: {
        element: 'select',
        value: '',
        label: true,
        labelText: 'Product',
        config: {
          name: 'name_input',
          options: list
        },
        validation: {
          required: false,
        },
        valid: true,
      }
    }});
  }

  updateForm(newState) {
    this.setState({
      formData: newState
    });
  }

  submitForm = (event) => {
    event.preventDefault();
    let datatosubmit = { quantity: this.state.formData.quantity.value, productName: this.state.formData.productName.value }
    let data = JSON.stringify(datatosubmit);
    let id = this.state.id;
    axios.defaults.headers.post['Content-Type'] = 'application/json';
    axios.post(`http://127.0.0.1:8080/achievements/${id}/achievement_conditions`, data, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Authorization': document.cookie
      }
    })
      .then(function (response) {
        const status = JSON.parse(response.status);
        console.log(response.status);

        if (status === +200) {
          window.location.replace('/achievements');
        }
      })
      .catch(function (error) {
        console.log(error.response);
        console.log(error);
        console.log(error.name);
        console.log(error.value);
        console.log(error.status);
        console.log(error.type);
      });
  }

  render() {
    return (
      <div>
        <form onSubmit={this.submitForm}>
          <FormFields
            formData={this.state.formData}
            change={(newState) => this.updateForm(newState)}
          />
          <button type="submit">Add condition</button>
        </form>
      </div>
    )
  }
}

export default CreateAchievementConditionForm;