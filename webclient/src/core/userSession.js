import globalApi from "./api";
import jwtDecode from "jwt-decode";

export const Rights = {
    MY_MEALS: "MY_MEALS",
    USER_MANAGEMENT: "USER_MANAGEMENT",
    MEAL_MANAGEMENT: "MEAL_MANAGEMENT",
}

export const Roles = {
    USER_MANAGER: "USER_MANAGER",
    ADMIN:"ADMIN",
    REGULAR_USER:"REGULAR_USER",
}

export const roleIdToName = (role)=>{
    switch(role) {
        case Roles.USER_MANAGER: return "User Manager";
        case Roles.ADMIN: return "Admin";
        default: return "Regular User"
    }
}

export function ShowWithRight({ right, children }) {
    if (userSession.hasRight(right)) {
        return children;
    }

    return null;
}

export class UserSession {
    constructor(api = globalApi){
        this.api = api;
    }   

    currentRole(){
        if (!this.isLoggedIn()) {
            return null;
        }

        return jwtDecode(this.api.getToken()).role || Roles.REGULAR_USER;
    }
    setToken(token){
        this.api.setToken(token);
    }
    
    isLoggedIn() {
        return this.api.hasToken();
    }

    logout(){
        this.api.clearToken();
    }

    hasRight(right) {
        if (!this.isLoggedIn()) {
            return false;
        }
        const rights = jwtDecode(this.api.getToken()).privileges || [];
        return rights.indexOf(right) >= 0;
    }
}
const userSession = new UserSession();
export default userSession;