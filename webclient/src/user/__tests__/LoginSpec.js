import React from "react";
import { shallow } from "enzyme";
import { Login } from "../Login";
import ValidationForm from "../../common/form/ValidationForm";
import { when } from "jest-when";
import { Rights } from "../../core/userSession";
import { BadRequestError, UnauthenticatedError } from "../../core/api";


describe("#Login", () => {
    const validationSectionParams = {
        onFieldChange: jest.fn(),
        data: {
            email: "un",
            password: "ps",
        },
        isValid: jest.fn().mockReturnValue(true),
        validationFields: {},
        validationMessage: null,
    }

    it("should render email and password", () => {
        const wrapper = shallow(<Login classes={{}} />);
        const validationForm = wrapper.find(ValidationForm);
        const validationSection = validationForm.renderProp("children")(validationSectionParams);
        expect(validationSection.find("[name='email']").prop("value")).toEqual("un");
        expect(validationSection.find("[name='password']").prop("value")).toEqual("ps");
    });

    describe("#onSubmit", () => {
        let userSession;
        let history;
        let postApi;
        beforeEach(() => {
            userSession = { setToken: jest.fn(), hasRight: jest.fn().mockReturnValue(true) };
            history = { replace: jest.fn() }
            postApi = jest.fn().mockReturnValue({ json: jest.fn().mockReturnValue({ token: "abc" }) });

        })

        async function submit(wrapper, email, password) {
            const validationForm = wrapper.find(ValidationForm);
            validationForm.prop("onDataChange")({
                email: email,
                password: password
            })

            const validationSection = validationForm.renderProp("children")(validationSectionParams);
            await validationSection.find("[type='submit']").simulate("click", { preventDefault: jest.fn() });
        }

        it("should submmit email and password", () => {
            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);

            submit(wrapper, "un", "ps");

            expect(postApi).toBeCalledWith("/v1/session", {
                email: "un",
                password: "ps",
            })
        })

        it("should set token to userSession if login successfuly", async () => {

            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);
            await submit(wrapper, "un", "ps");

            expect(userSession.setToken).toBeCalledWith("abc");
        })

        it("should navigate to My Meal page if has right", async () => {
            userSession.hasRight.mockReturnValue(true);
            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);
            await submit(wrapper, "un", "ps");

            expect(history.replace).toBeCalledWith("/meals");
        })

        it("should navigate to user if has right", async () => {
            when(userSession.hasRight).calledWith(Rights.MY_MEALS).mockReturnValue(false);
            when(userSession.hasRight).calledWith(Rights.USER_MANAGEMENT).mockReturnValue(true);

            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);
            await submit(wrapper, "un", "ps");

            expect(history.replace).toBeCalledWith("/users");
        })

        it("should set Server Error if return BadRequest", async () => {
            postApi = jest.fn().mockReturnValue(Promise.reject(new BadRequestError("Error", 401, { error: "errors" })));
            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);
            await submit(wrapper, "un", "ps");
            const validationForm = wrapper.find(ValidationForm);
            expect(validationForm.prop("serverValidationError")).toEqual("errors");
        })

        it("should handle if Login Failed (401)", async ()=>{
            postApi = jest.fn().mockReturnValue(Promise.reject(new UnauthenticatedError("Error")));
            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} />);
            await submit(wrapper, "un", "ps");
            const validationForm = wrapper.find(ValidationForm);
            expect(validationForm.prop("serverValidationError").message).toEqual("Wrong Email or Password");
        })

        it("other errors should be handled", async ()=>{
            const error = new Error("");
            postApi = jest.fn().mockReturnValue(Promise.reject(error));
            const handleErrorSpy = jest.fn();
            const wrapper = shallow(<Login classes={{}} api={{ post: postApi }} userSession={userSession} history={history} handleError={handleErrorSpy} />);
            await submit(wrapper, "un", "ps");

            expect(handleErrorSpy).toBeCalledWith(error);
        })
    })
})
