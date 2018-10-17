import React from 'react';

export default class QRCode extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let file = `./../header/${this.props.qrpath}`;
        return <img src={file} alt="QR" />;
    }
}
