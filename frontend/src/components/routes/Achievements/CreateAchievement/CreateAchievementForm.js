import React from 'react';
import FormFields from './../../widgets/forms/FormFields';
import axios from 'axios';
import './new.css';
import MyHeader from './../../../header/header';



class CreateAchievementForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      formData: {
        name: {
          element: 'input',
          value: '',
          label: true,
          labelText: 'name',
          config: {
            name: 'name_input',
            text: 'text',
            placeholder: 'enter a name'

          },
          validation: {
            required: false,
          },
          valid: true,
        },

        description: {
          element: 'textarea',
          value: '',
          label: true,
          labelText: '',
          config: {
            name: 'name_input',
            rows: 15,
            cols: 36

          },
          validation: {
            required: false,
          },
          valid: true,
        },

        xpValue: {
          element: 'input',
          value: '',
          label: true,
          labelText: 'xp value',
          config: {
            name: 'name_input',
            text: 'text',
            placeholder: 'enter xp value'

          },
          validation: {
            required: false,
          },
          valid: true,
        },

        expiration: {
          element: 'input',
          value: '',
          label: true,
          labelText: 'expiration date',
          config: {
            name: 'name_input',
            text: 'text',
            placeholder: 'enter expiration date'

          },
          validation: {
            required: false,
          },
          valid: true,
        }
      }
    }
  }

  updateForm(newState) {
    this.setState({
      formData: newState
    });

  }



  submitForm = (event) => {
    event.preventDefault();
    console.log(this.state.formData.name.value);
    let datatosubmit = { name: this.state.formData.name.value, description: this.state.formData.description.value, xpValue: this.state.formData.xpValue.value, expiration: this.state.formData.expiration.value }
    let data = JSON.stringify(datatosubmit);
    axios.defaults.headers.post['Content-Type'] = 'application/json';
    axios.post('http://127.0.0.1:8080/achievements', data, {
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
        <MyHeader />
        <form onSubmit={this.submitForm}>
          <FormFields
            formData={this.state.formData}
            change={(newState) => this.updateForm(newState)}
          />
          <button type="submit">Add achievement</button>
        </form>
      </div>
    )
  }
}
export default CreateAchievementForm;
