import { Rights } from "../core/userSession";

export const Pages = {
    MY_MEALS: "/meals",
    MY_NEW_MEAL: "/meals/new",
    MY_UPDATE_MEAL: "/meals/:id/update",
    MY_SETTINGS: "/users/settings",

    ALL_MEALS: "/meals/all",
    ALL_NEW_MEAL: "/meals/all/new",
    ALL_UPDATE_MEAL: "/meals/all/:id/update",

    USERS: "/users",
    UPDATE_USER: "/users/:id/update",
    NEW_USER: "/users/new",

    NOT_FOUND: "/not-found",
    LOGIN: "/users/login",
    REGISTER: "/users/register",
}

export function getDefaultPage(userSessionp) {
    if (userSessionp.hasRight(Rights.MEAL_MANAGEMENT)) {
        return Pages.ALL_MEALS;
    }
    if (userSessionp.hasRight(Rights.USER_MANAGEMENT)) {
        return Pages.USERS;
    }
    return Pages.MY_MEALS;
}