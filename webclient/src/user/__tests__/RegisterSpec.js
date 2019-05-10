import React from "react";
import { shallow } from "enzyme";
import { Register } from "../Register";
import ValidationForm from "../../common/form/ValidationForm";
import { FormHelperText } from "@material-ui/core";
import { BadRequestError } from "../../core/api";
import { ApiUrl } from '../../constants/ApiUrl';

describe("#Register", () => {
    let validationSectionParams = {
        onFieldChange: jest.fn(),
        data: {
            email: "un",
            fullName: "fullname",
            password: "ps",
        },
        isValid: jest.fn().mockReturnValue(true),
        validationFields: {
            email: "Email wrong",
        },
        validationMessage: null,
    }
    it("should render Form information", () => {
        const wrapper = shallow(<Register classes={{}} />);

        const validationForm = wrapper.find(ValidationForm);
        const validationSection = validationForm.renderProp("children")(validationSectionParams);
        expect(validationSection.find("[name='email']").prop("value")).toEqual("un");
        expect(validationSection.find("[name='fullName']").prop("value")).toEqual("fullname");
        expect(validationSection.find("[name='password']").prop("value")).toEqual("ps");
    });

    it("should handle onFieldChange properly", () => {
        const wrapper = shallow(<Register classes={{}} />);

        const validationForm = wrapper.find(ValidationForm);
        const validationSection = validationForm.renderProp("children")(validationSectionParams);

        expect(validationSection.find("[name='email']").simulate("change", { currentTarget: { value: "email1" } }))
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("email", "email1");
        expect(validationSection.find("[name='fullName']").simulate("change", { currentTarget: { value: "full1" } }))
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("fullName", "full1");
        expect(validationSection.find("[name='password']").simulate("change", { currentTarget: { value: "pass1" } }))
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("password", "pass1");
    })

    it("should handle error", () => {
        const wrapper = shallow(<Register classes={{}} />);

        const validationForm = wrapper.find(ValidationForm);
        const validationSection = validationForm.renderProp("children")(validationSectionParams);

        expect(validationSection.find("[name='email']").parent().prop("error")).toEqual(true);
        expect(validationSection.find(FormHelperText).at(0).childAt(0).text()).toEqual("Email wrong")
        expect(validationSection.find("[name='fullName']").parent().prop("error")).toEqual(false);
    });

    describe("#Submit", () => {
        let history;
        let postApi;
        beforeEach(() => {
            history = { replace: jest.fn() }
            postApi = jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ token: "abc" }) });

        })

        async function submit(wrapper, email, fullName, password) {
            const validationForm = wrapper.find(ValidationForm);
            validationForm.prop("onDataChange")({
                email: email,
                fullName: fullName,
                password: password
            })

            const validationSection = validationForm.renderProp("children")(validationSectionParams);
            await validationSection.find("[type='submit']").prop("onClick")({ preventDefault: jest.fn() });
        }

        it("should submmit correct info", async () => {
            const handleError = jest.fn();
            const wrapper = shallow(<Register handleError={handleError} classes={{}} api={{ post: postApi }} history={history} />);

            await submit(wrapper, "email1", "fullname1", "password1");

            expect(postApi).toBeCalledWith("/v1/users", {
                email: "email1",
                fullName: "fullname1",
                password: "password1",
            })
        })

        it("should login automatically", async () => {
            const userSession = { setToken: jest.fn(), hasRight: jest.fn().mockReturnValue(true) };
            const loginApi = jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: { accessToken: "abc" } }) })
            const showSuccessMessage = jest.fn();
            const wrapper = shallow(<Register showSuccessMessage={showSuccessMessage}
                userSession={userSession} classes={{}} api={{ post: postApi, login: loginApi }} history={history} />);

            await submit(wrapper, "email1", "fullname1", "password1");
            expect(postApi).toBeCalled();
            expect(loginApi).toHaveBeenCalledWith(ApiUrl.SESSION, {
                email: "email1",
                password: "password1",
            });
            expect(userSession.setToken).toHaveBeenCalledWith("abc");
            expect(history.replace).toHaveBeenCalledWith("/meals/all");
        })

        it("should set Server Error if return BadRequest", async () => {
            postApi = jest.fn().mockReturnValue(Promise.reject(new BadRequestError("Error", 401, { error: "errors" })));
            const wrapper = shallow(<Register classes={{}} api={{ post: postApi }} history={history} />);
            await submit(wrapper, "email1", "fullname1", "password1");
            const validationForm = wrapper.find(ValidationForm);
            expect(validationForm.prop("serverValidationError")).toEqual("errors");
        })

        it("other errors should be handled", async () => {
            const error = new Error("");
            postApi = jest.fn().mockReturnValue(Promise.reject(error));
            const handleErrorSpy = jest.fn();
            const wrapper = shallow(<Register classes={{}} api={{ post: postApi }} history={history} handleError={handleErrorSpy} />);
            await submit(wrapper, "un", "ps");

            expect(handleErrorSpy).toBeCalledWith(error);
        })

        it("should not submit if validation failed", async () => {
            validationSectionParams = {
                ...validationSectionParams,
                isValid: jest.fn().mockReturnValue(false),
            }

            const handleErrorSpy = jest.fn();
            const wrapper = shallow(<Register classes={{}} api={{ post: postApi }} history={history} handleError={handleErrorSpy} />);

            await submit(wrapper, "un", "ps");
            expect(postApi).not.toBeCalled();
        })
    });
})