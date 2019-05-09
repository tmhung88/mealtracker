import React from 'react';
import  NewMeal  from '../NewMeal';
import { ApiUrl } from '../../../constants/ApiUrl';
import { Pages } from '../../../constants/Pages';

export default class MyNewMeal extends React.Component {
    render(){
        return <NewMeal
        baseApiUrl={ApiUrl.ME_MEALS}
        cancelPage={Pages.MY_MEALS}
        />
    }
}
