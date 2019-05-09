import React from "react";
import { shallow } from "enzyme";
import { withPage } from "../AppPage";
import SnackbarErrorMessage from "../SnackbarErrorMessage";
import { ServerError, UnauthenticatedError, UnauthorizedError } from "../../api";

describe("#AppPage", () => {
    let WrapElement;
    let wrapper;
    let history;
    beforeEach(() => {
        history = {
            push:jest.fn(),
        };
        WrapElement = withPage(FakeComponent, { withRouter: (c) => c });
        wrapper = shallow(<WrapElement history={history}/>);
    })
    describe("Handle Error", () => {
        it("generic error with message", () => {
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")({ message: "any error" });
            const snackBarContent = wrapper.find(SnackbarErrorMessage)
            expect(snackBarContent.prop("message")).toEqual("any error");
        })

        it("stringify generic error", () => {
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")({ data: "any error" });
            const snackBarContent = wrapper.find(SnackbarErrorMessage)
            expect(snackBarContent.prop("message")).toEqual(JSON.stringify({ data: "any error" }));
        })

        it("handle Server Error without body", () => {
            const serverError = new ServerError("message 1", 200, null);
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            const snackBarContent = wrapper.find(SnackbarErrorMessage);
            expect(snackBarContent.prop("message")).toEqual("message 1");
        })

        it("Server Error body as json string", () => {
            const serverError = new ServerError("message 1", 200, JSON.stringify({ error: { message: "message 2" } }));
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            const snackBarContent = wrapper.find(SnackbarErrorMessage);
            expect(snackBarContent.prop("message")).toEqual("message 2");
        })

        it("Server Error body as text", () => {
            const serverError = new ServerError("message 1", 200, "message 2");
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            const snackBarContent = wrapper.find(SnackbarErrorMessage);
            expect(snackBarContent.prop("message")).toEqual("message 2");
        })

        it("Server Error body as object", () => {
            const serverError = new ServerError("message 1", 200, { error: { message: "message 2" } });
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            const snackBarContent = wrapper.find(SnackbarErrorMessage);
            expect(snackBarContent.prop("message")).toEqual("message 2");
        })

        it("error as UnauthorizedError should redirect to login", ()=>{
            const serverError = new UnauthenticatedError();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            
            expect(history.push).toHaveBeenCalledWith("/users/login");
        })

        it("error as UnauthorizedError should redirect to login", ()=>{
            const serverError = new UnauthorizedError();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            
            expect(history.push).toHaveBeenCalledWith("/users/login");
        })
    })

    describe("goBackOrReplace", ()=>{
        it("should goback if there is previous page", ()=>{
            history.length = 2;
            history.goBack = jest.fn();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("goBackOrReplace")("/page1");
            expect(history.goBack).toHaveBeenCalled();
        })

        it("should replace with provided page if there is not previous page", ()=>{
            history.length = 1;
            history.replace = jest.fn();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("goBackOrReplace")("/page1");
            expect(history.replace).toHaveBeenCalledWith("/page1");
        })
    })
})

class FakeComponent extends React.Component {
    render() {
        return <div>Data</div>
    }
}