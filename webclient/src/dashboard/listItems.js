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

export const mainListItems = (
    <div>
        <ListItemLink to="/meals">
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="Meals" />
        </ListItemLink>
        <ListItemLink to="/users">
            <ListItemIcon>
                <PeopleIcon />
            </ListItemIcon>
            <ListItemText primary="Users" />
        </ListItemLink>
    </div>
);
