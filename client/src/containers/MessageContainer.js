import React from 'react';
import _ from "lodash";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {readMention} from "../actions";
import MessageWithHighlight from "../components/MessageWithHighlight";
import Message from "../components/Message";

class MessageContainer extends React.Component {

    constructor(props) {
        super(props);
        this.itemRefs = {}
    }

    componentDidUpdate() {
        const {windowRef} = this.props
        const {messageId, mentionId} = this.props.mention;
        if (messageId !== undefined) {
            windowRef.current.scrollTo(0, (this.itemRefs[messageId].offsetTop - 37 - this.itemRefs[messageId].offsetHeight));
            this.props.readMention(messageId, mentionId);
        }
    }

    addItemRef(messageId){
        return (el) =>{
            this.itemRefs[messageId] = el;
        }
    }

    render() {
        const {mentionMessages} = this.props.chat;
        return _.map(this.props.chat.bodies, (v) => {
            if (mentionMessages[v.messageId] !== undefined) {
                return (
                    <MessageWithHighlight key={v.messageId} uid={v.messageId} addRef={this.addItemRef(v.messageId)} id={v.id} text={v.text}/>
                );
            } else {
                return (
                    <Message key={v.messageId} uid={v.messageId} addRef={this.addItemRef(v.messageId)} id={v.id} text={v.text}/>
                );
            }
        })
    }
}

function mapToPropsState(state) {
    return {chat: state.chat, mention: state.mention}
}

export default connect(mapToPropsState, d => bindActionCreators({readMention}, d))(MessageContainer);
