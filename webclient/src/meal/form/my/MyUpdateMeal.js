import React from 'react';
import  UpdateMeal  from '../UpdateMeal';

export default class MyUpdateMeal extends React.Component {
    render(){
        return <UpdateMeal
        baseApiUrl="/v1/users/me/meals"
        cancelPage="/meals"
        />
    }
}
