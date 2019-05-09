import React from "react";
import { shallow } from "enzyme";
import { MealForm } from "../MealForm";
import NotFoundForm from "../../../common/form/NotFoundForm";
import ValidationForm from "../../../common/form/ValidationForm";
import UserSelect from "../../../user/UserSelect";

describe("#MealForm", () => {
    const data = {
        id: 10,
        name: "Meal 1",
        consumedDate: "2018-05-01",
        consumedTime: "07:26",
        calories: 10,
        consumerId: 12,
        consumerEmail: "test@mail.com",
    }

    let validationSectionParams;

    function renderValidationForm(wrapper, data) {
        const validationForm = wrapper.find(ValidationForm);
        return validationForm.renderProp("children")(validationSectionParams);
    }

    beforeEach(() => {
        validationSectionParams = {
            onFieldChange: jest.fn(),
            onFieldsChange: jest.fn(),
            data: data,
            isValid: jest.fn().mockReturnValue(true),
            validationFields: {
                email: "Email wrong",
            },
            validationMessage: null,
        }
    })

    it("show not found view if notFound prop is true", () => {
        const wrapper = shallow(<MealForm classes={{}} notFound />);
        expect(wrapper.find(NotFoundForm)).toHaveLength(1);
    })

    it("should render all information", () => {
        const wrapper = shallow(<MealForm classes={{}} userSelect user={data} renderActionButtons={jest.fn()} />);
        const validationSection = renderValidationForm(wrapper, data)
        expect(validationSection.find(`[id="consumedDate"]`).prop("value")).toEqual("2018-05-01");
        expect(validationSection.find(`[id="consumedTime"]`).prop("value")).toEqual("07:26");
        expect(validationSection.find(`[id="name"]`).prop("value")).toEqual("Meal 1");
        expect(validationSection.find(`[id="calories"]`).prop("value")).toEqual(10);
        expect(validationSection.find(UserSelect).prop("user")).toEqual({ key: 12, label: "test@mail.com" });
    })

    it("should update user info on each field", () => {
        const wrapper = shallow(<MealForm classes={{}} userSelect user={data} renderActionButtons={jest.fn()} />);
        const validationSection = renderValidationForm(wrapper, data)
        const buildEvent = (value) => {
            return { currentTarget: { value } };
        }
        expect(validationSection.find(`[id="consumedDate"]`).simulate("change", buildEvent("2017-06-02")));
        expect(validationSection.find(`[id="consumedTime"]`).simulate("change", buildEvent("06:23")));
        expect(validationSection.find(`[id="name"]`).simulate("change", buildEvent("Meal 2")));
        expect(validationSection.find(`[id="calories"]`).simulate("change", buildEvent("33")));
        expect(validationSection.find(UserSelect).simulate("userChange", { key: 33, label: "test2@email.com" }));

        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("consumedDate", "2017-06-02");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("consumedTime", "06:23");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("name", "Meal 2");
        expect(validationSectionParams.onFieldChange).toHaveBeenCalledWith("calories", 33);
        expect(validationSectionParams.onFieldsChange).toHaveBeenCalledWith({
            consumerId: 33,
            consumerEmail: "test2@email.com",
        });
    })

    it("should not render user select if there is no userSelect prop", ()=>{
        const wrapper = shallow(<MealForm classes={{}} user={data} renderActionButtons={jest.fn()} />);
        const validationSection = renderValidationForm(wrapper, data)
        expect(validationSection.find(UserSelect)).toHaveLength(0);
    })
})