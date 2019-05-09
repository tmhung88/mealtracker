import React from 'react';
import  UpdateMeal  from '../UpdateMeal';
import { ApiUrl } from '../../../constants/ApiUrl';
import { Pages } from '../../../constants/Pages';

export default class ManagementUpdateMeal extends React.Component {
    render(){
        return <UpdateMeal
        baseApiUrl={ApiUrl.MEALS}
        cancelPage={Pages.ALL_MEALS}
        userSelect
        />
    }
}
