import api from "./api";
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

class UserSession {
    currentRole(){
        if (!this.isLoggedIn()) {
            return null;
        }

        return jwtDecode(api.getToken()).role || Roles.REGULAR_USER;
    }
    setToken(token){
        api.setToken(token);
    }
    isLoggedIn() {
        return api.hasToken();
    }

    logout(){
        api.clearToken();
    }

    hasRight(right) {
        if (!this.isLoggedIn()) {
            return false;
        }
        const rights = jwtDecode(api.getToken()).privileges || [];
        return rights.indexOf(right) >= 0;
    }
}
const userSession = new UserSession();
export default userSession;