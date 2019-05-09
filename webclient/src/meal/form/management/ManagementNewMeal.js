import React from 'react';
import NewMeal from '../NewMeal';
import { ApiUrl } from '../../../constants/ApiUrl';
import { Pages } from '../../../constants/Pages';

export default class ManagementNewMeal extends React.Component {
    render() {
        return <NewMeal
            baseApiUrl={ApiUrl.MEALS}
            cancelPage={Pages.ALL_MEALS}
            userSelect
        />
    }
}
