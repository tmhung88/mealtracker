import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import PeopleIcon from '@material-ui/icons/People';
import FastFoodIcon from '@material-ui/icons/Fastfood';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

function ListItemLink(props) {
    return <ListItem button component={Link} {...props} />;
}

export const MainListItems = ({selectedPathName}) => (
    <div>
        <ListItemLink to="/meals" selected={selectedPathName === "/meals"}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="Meals" />
        </ListItemLink>
        <ListItemLink to="/meals/all" selected={selectedPathName === "/meals/all"}>
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="All Meals" />
        </ListItemLink>
        <ListItemLink to="/users" selected={selectedPathName === "/users"}>
            <ListItemIcon>
                <PeopleIcon />
            </ListItemIcon>
            <ListItemText primary="Users" />
        </ListItemLink>

    </div>
);
