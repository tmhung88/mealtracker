import { UserSession, Roles } from "../userSession";
import jwt from "jwt-simple";

describe("#UserSession", () => {
    it("pass through api function", () => {
        const api = {
            getToken: jest.fn(),
            setToken: jest.fn(),
            clearToken: jest.fn(),
            hasToken: jest.fn().mockReturnValue(true),
        }

        const userSession = new UserSession(api);
        userSession.setToken("a");
        userSession.logout();

        expect(userSession.isLoggedIn()).toEqual(true);
        expect(api.setToken).toHaveBeenCalledWith("a");
        expect(api.clearToken).toHaveBeenCalled();
    })

    it("extract role from token", () => {
        const api = {
            getToken: jest.fn().mockReturnValue(jwt.encode({ role: "role-1" }, "a")),
            hasToken: jest.fn().mockReturnValue(true),
        }

        const userSession = new UserSession(api);
        expect(userSession.currentRole()).toEqual("role-1");
    })

    it("default role if role is empty in token", () => {
        const api = {
            getToken: jest.fn().mockReturnValue(jwt.encode({}, "a")),
            hasToken: jest.fn().mockReturnValue(true),
        }

        const userSession = new UserSession(api);
        expect(userSession.currentRole()).toEqual(Roles.REGULAR_USER);
    })

    it("should check right from token", () => {
        const api = {
            getToken: jest.fn().mockReturnValue(jwt.encode({ privileges: ["p1", "p2"] }, "a")),
            hasToken: jest.fn().mockReturnValue(true),
        }

        const userSession = new UserSession(api);
        expect(userSession.hasRight("p1")).toEqual(true);
        expect(userSession.hasRight("p3")).toEqual(false);
    })
})