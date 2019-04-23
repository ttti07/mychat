import React from 'react';
import {connect} from "react-redux";
import SelfMessage from "../components/SelfMessage";
import Message from "../components/Message";


class MessageContainer extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        if (this.props.profile.login === this.props.id) {
            return <SelfMessage name={this.props.id} message={this.props.message}/>;
        } else {
            return <Message name={this.props.id} message={this.props.message} />;
        }
    }

}


export default connect(s => s.oauth, null)(MessageContainer);
