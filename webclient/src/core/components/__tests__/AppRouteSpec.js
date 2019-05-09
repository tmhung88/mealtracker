import React from "react";
import { shallow } from "enzyme";
import { AppRoute } from "../AppRoute";
import { Route } from "react-router-dom";
import { Rights } from "../../userSession";

describe("#AppRoute", () => {
    it("should redirect if not logged in", () => {
        const userSession = {
            isLoggedIn: jest.fn().mockReturnValue(false),
        }
        const wrapper = shallow(<AppRoute userSession={userSession} />);
        const rendered = wrapper.find(Route).renderProp("render")({ location: "a" });
        expect(rendered.prop("to")).toEqual({ pathname: '/users/login', state: { from: 'a' } })
    })

    it("should redirect if user has no right", () => {
        const userSession = {
            isLoggedIn: jest.fn().mockReturnValue(true),
            hasRight: jest.fn().mockReturnValue(false)
        }
        const wrapper = shallow(<AppRoute right={Rights.MEAL_MANAGEMENT} userSession={userSession} />);
        const rendered = wrapper.find(Route).renderProp("render")({ location: "a" });
        expect(rendered.prop("to")).toEqual({ pathname: '/users/login', state: { from: 'a' } })
    })

    it("should render if user logged and has right", () => {
        const userSession = {
            isLoggedIn: jest.fn().mockReturnValue(true),
            hasRight: jest.fn().mockReturnValue(true)
        }
        const wrapper = shallow(<AppRoute right={Rights.MEAL_MANAGEMENT} userSession={userSession} component="div" />);
        const rendered = wrapper.find(Route).renderProp("render")({ location: "a" });
        expect(rendered.html()).toContain("<div");
    })

    it("should render if user logged and not right specify", () => {
        const userSession = {
            isLoggedIn: jest.fn().mockReturnValue(true),
            hasRight: jest.fn().mockReturnValue(false)
        }
        const wrapper = shallow(<AppRoute userSession={userSession} component="div" />);
        const rendered = wrapper.find(Route).renderProp("render")({ location: "a" });
        expect(rendered.html()).toContain("<div");
    })
})