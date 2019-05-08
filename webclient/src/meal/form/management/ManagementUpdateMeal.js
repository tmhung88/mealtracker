import React from 'react';
import  UpdateMeal  from '../UpdateMeal';

export default class ManagementUpdateMeal extends React.Component {
    render(){
        return <UpdateMeal
        baseApiUrl="/v1/meals"
        cancelPage="/meals/all"
        userSelect
        />
    }
}
