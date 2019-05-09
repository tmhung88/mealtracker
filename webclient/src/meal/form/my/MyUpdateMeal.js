import React from 'react';
import  UpdateMeal  from '../UpdateMeal';
import { ApiUrl } from '../../../constants/ApiUrl';
import { Pages } from '../../../constants/Pages';

export default class MyUpdateMeal extends React.Component {
    render(){
        return <UpdateMeal
        baseApiUrl={ApiUrl.ME_MEALS}
        cancelPage={Pages.MY_MEALS}
        />
    }
}
