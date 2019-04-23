import * as at from '../constants/ActionTypes';

const defaultState = {
    bodies: [],
    emoji: []
};

export default function (state = defaultState, action) {
    switch (action.type) {
        case at.RECV_TEXT:
            console.log("--", action);
            return {
                bodies: [
                    ...state.bodies, {
                        id: action.payload.username,
                        text: action.payload.text,
                    }]
            };
        case at.RECV_EMOJI:
            console.debug("--", action);
            return {
                emoji: [
                    ...state.emoji, {
                        text: action.payload.text
                    }
                ]
            };
        default:
            return state;
    }
};
