import { hasToken, getToken } from "./api";
import jwtDecode from "jwt-decode";

export const Rights = {
    MyMeal: "MyMeal",
    UserManagement: "UserManagement",
    AllMeal: "AllMeal",
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