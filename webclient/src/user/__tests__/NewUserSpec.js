import React from "react";
import { shallow } from "enzyme";
import { NewUser } from "../NewUser";
import UserForm from "../UserForm";
import Bluebird from "bluebird";
import { BadRequestError } from "../../core/api";

describe("#NewUser", () => {
    it("should submit user data from user form", async () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn() };
        const handleError = jest.fn();
        const showSuccessMessage = jest.fn();
        const wrapper = shallow(<NewUser
            showSuccessMessage={showSuccessMessage}
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
        />);

        const userForm = wrapper.find(UserForm);
        userForm.simulate("userChange", { userData: "user" });

        await submit(wrapper);
        expect(handleError).not.toBeCalled();
        expect(api.post).toHaveBeenCalledWith("/v1/users", { userData: "user" });
        expect(goBackOrReplace).toHaveBeenCalledWith("/users");
        expect(showSuccessMessage).toHaveBeenCalledWith("Add User successfully");
    })

    it("should handle bad request error", async () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn().mockRejectedValue(new BadRequestError("", 11, { error: { errorHere: true } })) };
        const handleError = jest.fn();
        const wrapper = shallow(<NewUser
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
        />);


        await submit(wrapper);
        const userForm = wrapper.find(UserForm);
        expect(userForm.prop("serverValidationError")).toEqual({ errorHere: true });

    })

    it("should handle error on submit", async () => {
        const goBackOrReplace = jest.fn();
        const api = { post: jest.fn().mockRejectedValue({ error: true }) };
        const handleError = jest.fn();
        const wrapper = shallow(<NewUser
            classes={{}}
            api={api}
            handleError={handleError}
            goBackOrReplace={goBackOrReplace}
        />);

        await submit(wrapper);
        expect(handleError).toHaveBeenCalledWith({ error: true });
    })
})

async function submit(wrapper) {
    const userForm = wrapper.find(UserForm);
    const renderActionButtons = userForm.renderProp("renderActionButtons")(jest.fn().mockReturnValue(true));
    renderActionButtons.find(`[type="submit"]`).simulate("click", { preventDefault: jest.fn() });
    await Bluebird.delay(10);
}