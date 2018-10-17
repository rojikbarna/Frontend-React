import React, { Component } from 'react';
import FormFields from './widgets/forms/FormFields';
import axios from 'axios'

class Registration extends Component {
    constructor(props) {
        super(props);
        this.state = {
            formData: {
                firstName: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'First Name',
                    config: {
                        name: 'firstName_input',
                        type: 'text',
                        placeholder: 'Enter your First Name'
                    },
                    validation: {
                        required: true,
                        minLen: 3
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                lastName: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Last Name',
                    config: {
                        name: 'lastName_input',
                        type: 'text',
                        placeholder: 'Enter your Last Name'
                    },
                    validation: {
                        required: true,
                        minLen: 3
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                nickName: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Nick Name',
                    config: {
                        name: 'nickName_input',
                        type: 'text',
                        placeholder: 'Enter your Nick Name'
                    },
                    validation: {
                        required: true,
                        minLen: 3
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                password: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Password',
                    config: {
                        name: 'password_input',
                        type: 'password',
                        placeholder: 'Enter your password'
                    },
                    validation: {
                        pass: true,
                        required: true,
                        minLen: 4
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                passwordagain: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Confirm Password',
                    config: {
                        name: 'password_input',
                        type: 'password',
                        placeholder: 'Enter your password'
                    },
                    validation: {
                        passagain:true,
                        required: true,
                        minLen: 4
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                email: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Email',
                    config: {
                        name: 'email_input',
                        type: 'email',
                        placeholder: 'Enter your email'
                    },
                    validation: {
                        email:true,
                        required: true,
                        minLen: 5
                    },
                    valid: false,
                    touched: false,
                    validationMessage: ''
                },
                gender: {
                    element: 'select',
                    value: '',
                    label: true,
                    labelText: 'Gender',
                    config: {
                        name: 'gender_input',
                        options: [
                            { val: '0', text: 'select gender' },
                            { val: true, text: 'male' },
                            { val: false, text: 'female' }
                        ],
                    },
                    validation: {
                        required: false,
                    },
                    valid: true,
                    touched: false,
                    validationMessage: ''
                },
                date: {
                    element: 'input',
                    value: '',
                    label: true,
                    labelText: 'Date of birth',
                    config: {
                        name: 'dob_input',
                        type: 'text',
                        placeholder: 'YYYY-MM-DD'
                    },
                    validation: {
                        date:true,
                        required: false,
                        minLen: 3
                    },
                    valid: true,
                    touched: false,
                    validationMessage: ''
                },
            }
        }
        this.handleChange = this.handleChange.bind(this);
    }
    handleChange = (date) => {
        this.setState({
            startDate: date
        });

    }

    updateForm = (newState) => {
        this.setState({
            formData: newState
        })
    }
    submitForm = (event) => {
        event.preventDefault();
        let dataToSubmit = { xp: -999999999, role: "ADMIN", firstName: this.state.formData.firstName.value, lastName: this.state.formData.lastName.value, nickName: this.state.formData.nickName.value, password: this.state.formData.password.value, email: this.state.formData.email.value, dob: this.state.formData.date.value, gender: this.state.formData.gender.value };
        let fromIsValid = true;
        let data = JSON.stringify(dataToSubmit)
        console.log(data)
        for (let key in this.state.formData) {
            fromIsValid = this.state.formData[key].valid && fromIsValid;
        }
        if (fromIsValid) {
        axios.post('http://localhost:8080/users', data, {
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(function (response) {
                console.log(response);
                const status = JSON.parse(response.status);
                console.log(response.status);

                if (status === +200) {
                    window.location.replace('/home');
                }
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
    }

    render() {
        return (
            <div className='container'>
                <form onSubmit={this.submitForm}>
                    <FormFields
                        formData={this.state.formData}
                        change={(newState) => this.updateForm(newState)}
                    />
                    <button type='submit'>Submit</button>
                </form>
            </div>
        )
    }
}

export default Registration;