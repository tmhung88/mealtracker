import { getDefaultPage, Pages } from "../Pages";
import { when } from "jest-when";
import { Rights } from "../../core/userSession";

describe("#getDefaultPage", ()=>{
    it("return All meal if has all meals right", ()=>{
        const userSession = {
            hasRight:jest.fn(),
        };
        when(userSession.hasRight).calledWith(Rights.MEAL_MANAGEMENT).mockReturnValue(true);
        expect(getDefaultPage(userSession)).toEqual(Pages.ALL_MEALS);
    })

    it("return user list if has user mangement right", ()=>{
        const userSession = {
            hasRight:jest.fn(),
        };
        when(userSession.hasRight).calledWith(Rights.USER_MANAGEMENT).mockReturnValue(true);
        expect(getDefaultPage(userSession)).toEqual(Pages.USERS);
    })

    it("return my meals otherwise", ()=>{
        const userSession = {
            hasRight:jest.fn().mockReturnValue(false),
        };
        expect(getDefaultPage(userSession)).toEqual(Pages.MY_MEALS);
    })
})