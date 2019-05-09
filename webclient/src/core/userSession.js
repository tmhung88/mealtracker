import { hasToken, getToken, setToken, clearToken } from "./api";
import jwtDecode from "jwt-decode";

export const Rights = {
    MyMeal: "MY_MEALS",
    UserManagement: "USER_MANAGEMENT",
    AllMeal: "MEAL_MANAGEMENT",
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

        return jwtDecode(getToken()).role || Roles.REGULAR_USER;
    }
    setToken(token){
        setToken(token);
    }
    isLoggedIn() {
        return hasToken();
    }

    logout(){
        clearToken();
    }

    hasRight(right) {
        if (!this.isLoggedIn()) {
            return false;
        }
        const rights = jwtDecode(getToken()).rights || [];
        return rights.indexOf(right) >= 0;
    }
}
const userSession = new UserSession();
export default userSession;