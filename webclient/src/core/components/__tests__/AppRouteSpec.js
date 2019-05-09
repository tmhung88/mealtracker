import React from "react";
import { shallow } from "enzyme";
import { AppRoute } from "../AppRoute";
import { Route } from "react-router-dom";

describe("#AppRoute", () => {
    
    it("should redirect if not logged in", ()=>{
        const userSession = {
            isLoggedIn: jest.fn().mockReturnValue(false),
        }

        const wrapper = shallow(<AppRoute userSession={userSession} />);
        const rendered = wrapper.find(Route).renderProp("render");
        console.log(rendered);
    })
})