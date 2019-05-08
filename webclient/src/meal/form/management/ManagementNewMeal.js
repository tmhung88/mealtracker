import React from 'react';
import NewMeal from '../NewMeal';

export default class ManagementNewMeal extends React.Component {
    render() {
        return <NewMeal
            baseApiUrl="/v1/meals"
            cancelPage="/meals/all"
            userSelect
        />
    }
}
