import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import PeopleIcon from '@material-ui/icons/People';
import FastFoodIcon from '@material-ui/icons/Fastfood';
import { Link } from "react-router-dom";
import { ShowWithRight, Rights } from '../core/userSession';

function ListItemLink(props) {
    
    return <ShowWithRight right={props.right}>
        <ListItem  button component={Link} {...props} />
    </ShowWithRight>
}

export const MainListItems = ({selectedPathName}) => (
    <div>
        <ListItemLink right={Rights.MyMeal} to="/meals" selected={selectedPathName === "/meals"}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="Meals" />
        </ListItemLink>
        <ListItemLink right={Rights.AllMeal} to="/meals/all" selected={selectedPathName === "/meals/all"}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="All Meals" />
        </ListItemLink>
        <ListItemLink right={Rights.UserManagement} to="/users" selected={selectedPathName === "/users"}>
            <ListItemIcon>
                <PeopleIcon />
            </ListItemIcon>
            <ListItemText primary="Users" />
        </ListItemLink>

    </div>
);
