import React from 'react';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {clickMention} from "../actions";

class MentionWindowContainer extends React.Component {

    render() {
        const {mentions, clickMention} = this.props;
        const keys = Object.keys(mentions);
        console.debug("container", mentions);
        return (
            <div className="mt-4">
                {_.map(keys, id => {
                    return (
                        <div className="alert alert-primary" key={id}
                             onClick={event => {
                                 clickMention(mentions[id].messageId, mentions[id].mentionId)
                             }}>
                            <span className="font-weight-bold">{mentions[id].from}</span> 님이 언급하셨습니다.
                        </div>
                    )
                })
                }
            </div>
        )
    }
}

export default connect(s => s.mention, d => bindActionCreators({clickMention}, d))(MentionWindowContainer);
