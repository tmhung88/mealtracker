import { hasToken, getToken } from "./api";
import jwtDecode from "jwt-decode";

export const Rights = {
    MyMeal: "MY_MEALS",
    UserManagement: "USER_MANAGEMENT",
    AllMeal: "MEAL_MANAGEMENT",
}

export function ShowWithRight({ right, children }) {
    if (userSession.hasRight(right)) {
        return children;
    }

    return null;
}

class UserSession {
    isLoggedIn() {
        return hasToken();
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