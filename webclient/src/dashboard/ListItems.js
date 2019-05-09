import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import PeopleIcon from '@material-ui/icons/People';
import FastFoodIcon from '@material-ui/icons/Fastfood';
import { Link } from "react-router-dom";
import { ShowWithRight, Rights } from '../core/userSession';
import { Pages } from '../constants/Pages';

function ListItemLink(props) {
    
    return <ShowWithRight right={props.right}>
        <ListItem  button component={Link} {...props} />
    </ShowWithRight>
}

export const MainListItems = ({selectedPathName}) => (
    <div>
        <ListItemLink right={Rights.MY_MEALS} to={Pages.MY_MEALS} selected={selectedPathName === Pages.MY_MEALS}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="Meals" />
        </ListItemLink>
        <ListItemLink right={Rights.MEAL_MANAGEMENT} to={Pages.ALL_MEALS} selected={selectedPathName === Pages.ALL_MEALS}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="All Meals" />
        </ListItemLink>
        <ListItemLink right={Rights.USER_MANAGEMENT} to={Pages.USERS} selected={selectedPathName === Pages.USERS}>
            <ListItemIcon>
                <PeopleIcon />
            </ListItemIcon>
            <ListItemText primary="Users" />
        </ListItemLink>

    </div>
);
