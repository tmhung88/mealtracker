import React from "react";
import { shallow } from "enzyme";
import { withPage } from "../AppPage";
import { ServerError, UnauthenticatedError, UnauthorizedError } from "../../api";

describe("#AppPage", () => {
    let WrapElement;
    let wrapper;
    let history;
    let showError;
    let showSuccess;
    beforeEach(() => {
        history = {
            push: jest.fn(),
        };
        showError = jest.fn();
        showSuccess = jest.fn();
        WrapElement = withPage(FakeComponent, { withRouter: (c) => c, connect: c => c =>c });
        wrapper = shallow(<WrapElement history={history} showError={showError} showSuccess={showSuccess} />);
    })

    it("should handle success message", ()=>{
        const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("showSuccessMessage")("any message");
            expect(showSuccess).toHaveBeenCalledWith("any message");            
    })

    describe("Handle Error", () => {
        it("generic error with message", () => {
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")({ message: "any error" });
            expect(showError).toHaveBeenCalledWith("any error");            
        })

        it("stringify generic error", () => {
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")({ data: "any error" });
            expect(showError).toHaveBeenCalledWith(JSON.stringify({ data: "any error" }));
        })

        it("handle Server Error without body", () => {
            const serverError = new ServerError("message 1", 200, null);
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            expect(showError).toHaveBeenCalledWith("message 1");
        })

        it("Server Error body as json string", () => {
            const serverError = new ServerError("message 1", 200, JSON.stringify({ error: { message: "message 2" } }));
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            expect(showError).toHaveBeenCalledWith("message 2");
        })

        it("Server Error body as text", () => {
            const serverError = new ServerError("message 1", 200, "message 2");
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            expect(showError).toHaveBeenCalledWith("message 2");
        })

        it("Server Error body as object", () => {
            const serverError = new ServerError("message 1", 200, { error: { message: "message 2" } });
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);
            expect(showError).toHaveBeenCalledWith("message 2");
        })

        it("error as UnauthorizedError should redirect to login", () => {
            const serverError = new UnauthenticatedError();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);

            expect(history.push).toHaveBeenCalledWith("/users/login");
        })

        it("error as UnauthorizedError should redirect to login", () => {
            const serverError = new UnauthorizedError();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("handleError")(serverError);

            expect(history.push).toHaveBeenCalledWith("/users/login");
        })
    })

    describe("goBackOrReplace", () => {
        it("should goback if there is previous page", () => {
            history.length = 2;
            history.goBack = jest.fn();
            const fakedComponent = wrapper.find(FakeComponent);
            fakedComponent.prop("goBackOrReplace")("/page1");
            expect(history.goBack).toHaveBeenCalled();
        })

        it("should replace with provided page if there is not previous page", () => {
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