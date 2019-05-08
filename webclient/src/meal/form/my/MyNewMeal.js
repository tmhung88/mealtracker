import React from 'react';
import  NewMeal  from '../NewMeal';

export default class MyNewMeal extends React.Component {
    render(){
        return <NewMeal
        baseApiUrl="/v1/users/me/meals"
        cancelPage="/meals"
        />
    }
}
