import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';
import FastFoodIcon from '@material-ui/icons/Fastfood';

function ListItemLink(props) {
    return <ListItem button component="a" {...props} />;
}

export const mainListItems = (
    <div>
        <ListItemLink href="/meals">
            <ListItemIcon>
                <FastFoodIcon />
            </ListItemIcon>
            <ListItemText primary="Meals" />
        </ListItemLink>
        <ListItemLink href="/new-meal">
            <ListItemIcon>
                <PeopleIcon />
            </ListItemIcon>
            <ListItemText primary="Users" />
        </ListItemLink>
    </div>
);
