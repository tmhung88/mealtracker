import React from "react";
import { shallow } from "enzyme";
import { UpdateUser } from "../UpdateUser";
import Bluebird from "bluebird";
import UserForm from "../UserForm";
import { BadRequestError, NotFoundRequestError } from "../../core/api";

describe("#UpdateUser", () => {
    describe("on Submit", () => {
        it("should submit user data from user form", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                put: jest.fn(),
                get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: { userData: "user" } }) }),
            };
            const handleError = jest.fn();
            const showSuccessMessage = jest.fn();
            const wrapper = shallow(<UpdateUser
                showSuccessMessage={showSuccessMessage}
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);

            const userForm = wrapper.find(UserForm);
            userForm.simulate("userChange", { userData: "user" });

            await submit(wrapper);
            expect(handleError).not.toBeCalled();
            expect(api.put).toHaveBeenCalledWith("/v1/users/12", { userData: "user" });
            expect(goBackOrReplace).toHaveBeenCalledWith("/users");
            expect(showSuccessMessage).toHaveBeenCalledWith("Update User successfully");

        })

        it("should handle bad request error", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                get: jest.fn(),
                put: jest.fn().mockRejectedValue(new BadRequestError("", 11, { error: { errorHere: true } })),
            };
            const handleError = jest.fn();
            const wrapper = shallow(<UpdateUser
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);

            await submit(wrapper);
            const userForm = wrapper.find(UserForm);
            expect(userForm.prop("serverValidationError")).toEqual({ errorHere: true });
        })

        it("should handle error on submit", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                get: jest.fn(),
                put: jest.fn().mockRejectedValue({ error: true }),
            };
            const handleError = jest.fn();
            const wrapper = shallow(<UpdateUser
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);


            await submit(wrapper);
            expect(handleError).toHaveBeenCalledWith({ error: true });
        })
    })
    describe("on Fetch Data", () => {
        it("should fetch data from server", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                put: jest.fn(),
                get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data:{userData: "user"} }) }),
            };
            const handleError = jest.fn();
            const wrapper = shallow(<UpdateUser
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);

            await Bluebird.delay(10);

            const userForm = wrapper.find(UserForm);
            expect(userForm.prop("user")).toEqual({ userData: "user" });
            expect(api.get).toHaveBeenCalledWith("/v1/users/12");
        });

        it("should handle notfound response", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                put: jest.fn(),
                get: jest.fn().mockRejectedValue(new NotFoundRequestError())
            };
            const handleError = jest.fn();
            const wrapper = shallow(<UpdateUser
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);

            await Bluebird.delay(10);
            const userForm = wrapper.find(UserForm);
            expect(userForm.prop("notFound")).toEqual(true);
        })

        it("should handle other error on response", async () => {
            const goBackOrReplace = jest.fn();
            const api = {
                put: jest.fn(),
                get: jest.fn().mockRejectedValue({ error: true })
            };
            const handleError = jest.fn();
            shallow(<UpdateUser
                classes={{}}
                api={api}
                handleError={handleError}
                goBackOrReplace={goBackOrReplace}
                match={{ params: { id: "12" } }}
            />);

            await Bluebird.delay(10);
            expect(handleError).toHaveBeenCalledWith({ error: true });
        })
    })

})

async function submit(wrapper) {
    const userForm = wrapper.find(UserForm);
    const renderActionButtons = userForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
    renderActionButtons.find(`[type="submit"]`).simulate("click", { preventDefault: jest.fn() });
    await Bluebird.delay(10);
}