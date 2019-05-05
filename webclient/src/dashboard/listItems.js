import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import PeopleIcon from '@material-ui/icons/People';
import FastFoodIcon from '@material-ui/icons/Fastfood';
import { Link } from "react-router-dom";
import { ShowWithRight, Rights } from '../userSession';

function ListItemLink(props) {
    return <ListItem button component={Link} {...props} />;
}

export const MainListItems = ({ selectedPathName }) => (
    <div>
        <ShowWithRight right={Rights.MyMeal}>
            <ListItemLink to="/meals" selected={selectedPathName === "/meals"}>
                <ListItemIcon>
                    <FastFoodIcon />
                </ListItemIcon>
                <ListItemText primary="Meals" />
            </ListItemLink>
        </ShowWithRight>

        <ShowWithRight right={Rights.AllMeal}>
            <ListItemLink to="/meals/all" selected={selectedPathName === "/meals/all"}>
                <ListItemIcon>
                    <FastFoodIcon />
                </ListItemIcon>
                <ListItemText primary="All Meals" />
            </ListItemLink>
        </ShowWithRight>
        <ShowWithRight right={Rights.UserManagement}>
            <ListItemLink to="/users" selected={selectedPathName === "/users"}>
                <ListItemIcon>
                    <PeopleIcon />
                </ListItemIcon>
                <ListItemText primary="Users" />
            </ListItemLink>
        </ShowWithRight>
    </div>
);
