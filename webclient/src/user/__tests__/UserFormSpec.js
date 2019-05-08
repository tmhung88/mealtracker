import React from "react";
import { shallow } from "enzyme";
import { UserForm } from "../UserForm";
import NotFoundForm from "../../common/form/NotFoundForm";
import ValidationForm from "../../common/form/ValidationForm";
import { Roles } from "../../userSession";
import { MenuItem, Select } from "@material-ui/core";

describe("#UserForm", () => {
    const data = {
        email: "email1",
        password: "password1",
        fullName: "fullname1",
        role: Roles.ADMIN,
        dailyCalorieLimit: 10,
    }

    let validationSectionParams;

    function renderValidationForm(wrapper, data) {
        const validationForm = wrapper.find(ValidationForm);
        return validationForm.renderProp("children")(validationSectionParams);
    }

    beforeEach(() => {
        validationSectionParams = {
            onFieldChange: jest.fn(),
            data: data,
            isValid: jest.fn().mockReturnValue(true),
            validationFields: {
                email: "Email wrong",
            },
            validationMessage: null,
        }
    })

    it("show not found view if notFound prop is true", () => {
        const wrapper = shallow(<UserForm classes={{}} notFound />);
        expect(wrapper.find(NotFoundForm)).toHaveLength(1);
    })

    it("should render all information", () => {

        const userSession = { currentRole: jest.fn().mockReturnValue(Roles.ADMIN) };

        const wrapper = shallow(<UserForm classes={{}} user={data} renderActionButtons={jest.fn()} userSession={userSession} />);
        const validationSection = renderValidationForm(wrapper, data)
        expect(validationSection.find(`[id="email"]`).prop("value")).toEqual("email1");
        expect(validationSection.find(`[id="fullName"]`).prop("value")).toEqual("fullname1");
        expect(validationSection.find(`[id="dailyCalorieLimit"]`).prop("value")).toEqual(10);
        expect(validationSection.find(`[id="password"]`).prop("value")).toEqual("password1");
        expect(validationSection.find(Select).prop("value")).toEqual(Roles.ADMIN);
    })


    it("should update user info on each field", () => {
        const userSession = { currentRole: jest.fn().mockReturnValue(Roles.ADMIN) };

        const wrapper = shallow(<UserForm classes={{}} user={data} renderActionButtons={jest.fn()} userSession={userSession} />);
        const validationSection = renderValidationForm(wrapper, data)
        const buildEvent = (value) => {
            return { currentTarget: { value } };
        }
        expect(validationSection.find(`[id="email"]`).simulate("change", buildEvent("email2")));
        expect(validationSection.find(`[id="fullName"]`).simulate("change", buildEvent("name2")));
        expect(validationSection.find(`[id="dailyCalorieLimit"]`).simulate("change", buildEvent("22")));
        expect(validationSection.find(`[id="password"]`).simulate("change", buildEvent("pass2")));
        expect(validationSection.find(Select).simulate("change", { target: { value: Roles.REGULAR_USER } }));

        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("email", "email2");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("fullName", "name2");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("dailyCalorieLimit", 22);
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("password", "pass2");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("role", Roles.REGULAR_USER);
    })

    describe("should render role select items based on current role", () => {
        it("regular user", () => {
            const userSession = { currentRole: jest.fn().mockReturnValue(Roles.REGULAR_USER) };

            const wrapper = shallow(<UserForm classes={{}} user={data} renderActionButtons={jest.fn()} userSession={userSession} />);
            const validationSection = renderValidationForm(wrapper, data);

            expect(validationSection.find(MenuItem)).toHaveLength(0);
        })

        it("user manager", () => {
            const userSession = { currentRole: jest.fn().mockReturnValue(Roles.USER_MANAGER) };

            const wrapper = shallow(<UserForm classes={{}} user={data} renderActionButtons={jest.fn()} userSession={userSession} />);
            const validationSection = renderValidationForm(wrapper, data);

            expect(validationSection.find(MenuItem)).toHaveLength(2);
            expect(validationSection.find(`[value="${Roles.REGULAR_USER}"]`)).toHaveLength(1);
            expect(validationSection.find(`[value="${Roles.USER_MANAGER}"]`)).toHaveLength(1);
        })

        it("admin", () => {
            const userSession = { currentRole: jest.fn().mockReturnValue(Roles.ADMIN) };

            const wrapper = shallow(<UserForm classes={{}} user={data} renderActionButtons={jest.fn()} userSession={userSession} />);
            const validationSection = renderValidationForm(wrapper, data);

            expect(validationSection.find(MenuItem)).toHaveLength(3);
            expect(validationSection.find(MenuItem).find(`[value="${Roles.REGULAR_USER}"]`)).toHaveLength(1);
            expect(validationSection.find(MenuItem).find(`[value="${Roles.USER_MANAGER}"]`)).toHaveLength(1);
            expect(validationSection.find(MenuItem).find(`[value="${Roles.ADMIN}"]`)).toHaveLength(1);
        })

    });
})
