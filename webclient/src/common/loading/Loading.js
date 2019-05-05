import React from 'react';
import LoadingOverlay from 'react-loading-overlay';

export function Loading(props) {
    return (<LoadingOverlay
        active={props.active}
        styles={{
            overlay: (base) => ({
                ...base,
                background: 'rgba(255, 255, 255, 0.9)'
            }),
            spinner: (base) => ({
                ...base,
                width: '30px',
                '& svg circle': {
                    stroke: 'rgba(0, 0, 0, 0.5)'
                }
            })
        }}
        spinner
    >
        {props.children}
    </LoadingOverlay>)
}