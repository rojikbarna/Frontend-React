import React from 'react';

const FormFields = (props) => {
  const renderFields = () => {
    const formArray = [];

    for (let elementName in props.formData) {
      formArray.push({
        id: elementName,
        settings: props.formData[elementName]
      });
    }

    return formArray.map((item, i) => {
      return (
        <div key={i} className='form_element' >
          {renderTemplates(item)}
        </div>
      );
    });
  };

  const showLabel = (show, label) => {
    return show
      ? <label>{label}</label>
      : null;
  };

  const changeHandler = (event, id) => {
    const newState = props.formData;
    newState[id].value = event.target.value;
    let validData = validate(newState[id]);
    newState[id].valid = validData[0];
    newState[id].validationMessage = validData[1];
    props.change(newState);
  };
  const validate = (element) => {
    
    const emailregex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const passregex = /((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]))/;
    const dateregex = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/;
    let error = [true, ''];
    
    if (element.validation.passagain){
      var valid = false;
      const valid2 = props.formData.passwordagain.value;
      const valid1 = props.formData.password.value;
      if (valid2 === valid1){
        valid = true;
      }
      const message = `${!valid ? 'must be the same as the password': ''}`;
      error = !valid ? [valid, message] : error;
    }
    if (element.validation.date){
      const valid = dateregex.test(String(element.value));
      const message = `${!valid ? 'must be YYYY-MM-DD': ''}`;
      error = !valid ? [valid, message] : error;
    }
    if (element.validation.pass){
      const valid = passregex.test(String(element.value));
      const message = `${!valid ? 'must be at least 2 lower, upper, spacial, and number char ': ''}`;
      error = !valid ? [valid, message] : error;
    }
    if (element.validation.email){
      const valid = emailregex.test(String(element.value).toLocaleLowerCase());
      const message = `${!valid ? 'must be a valid email': ''}`;
      error = !valid ? [valid, message] : error;
     }
    if (element.validation.minLen) {
      const valid = element.value.length >= element.validation.minLen;
      const message = `${!valid ? 'Must be gretaer than ' + element.validation.minLen : ''}`;
      error = !valid ? [valid, message] : error;
    }
    if (element.validation.required) {
      const valid = element.value.trim() !== '';
      const message = `${!valid ? 'This field is required' : ''}`;
      error = !valid ? [valid, message] : error;
    }
    return error;
  };
  const showValidation = (data) => {
    let errorMessage = null;
    if (data.validation && !data.valid) {
      errorMessage = (
        <div className='label_error'>
          {data.validationMessage}
        </div>
      );
    }
    return errorMessage;
  };

  const renderTemplates = (data) => {
    let formTemplate = '';
    let values = data.settings;
    switch (values.element) {
      case ('input'):
        formTemplate = (
          <div>
            {showLabel(values.label, values.labelText)}
            <input
              {...values.config}
              value={values.value}
              onChange={
                (event) => changeHandler(event, data.id)
              }
            />
            {showValidation(values)}
          </div>
        );
        break;
      case ('select'):
        formTemplate = (
          <div>
            {showLabel(values.label, values.labelText)}
            <select
              value={values.value}
              name={values.config.name}
              onChange={
                (event) => changeHandler(event, data.id)
              }
            >
              {values.config.options.map((item, i) => (
                <option key={i} value={item.val}>
                  {item.text}
                </option>
              ))}
            </select>
          </div>
        );
        break;

      case ('textarea'):
        formTemplate = (
          <div>
            <p>description</p>
            {showLabel(values.label, values.labelText)}
            <textarea
              {...values.config}
              value={values.value}
              onChange={
                (event) => changeHandler(event, data.id)
              } />
          </div>
        );
        break;

      default:
        formTemplate = null;
    }
    return formTemplate;
  };

  return (
    <div>
      {renderFields()}
    </div>

  );
};

export default FormFields;
