import React from 'react';
import _ from 'lodash';
import MessageContainer from "../containers/MessageContainer";

export default ({windowRef, bodies}) => (
    <div className="border border-dark rounded" style={{
        width: '100%',
        height: '20rem',
        paddingLeft: '0.5rem',
        paddingTop: '0.25rem',
        overflow: 'auto'
    }} ref={windowRef}>
        {_.map(bodies, (v) => {
            return (
                <MessageContainer id={v.id} message={v.text}/>
            );
        })}
    </div>
);
