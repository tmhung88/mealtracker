import React from "react";
import { shallow, render } from "enzyme";
import { ValidationForm } from "../ValidationForm";

describe("#ValidationForm", () => {
    const data = {
        name: "",
        email: "",
        pass: "",
    };

    const constraints = {
        name: { presence: { allowEmpty: false } },
        email: { presence: { allowEmpty: false } },
        extra: { presence: { allowEmpty: false } },
    }

    it("should not set validation errors when field is not dirty", () => {
        const childrenFunc = jest.fn();
        render(<ValidationForm classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        const { validationFields, data:localData } = childrenFunc.mock.calls[0][0];
        expect(childrenFunc).toHaveBeenCalled();
        expect(validationFields).toHaveProperty("extra");
        expect(validationFields).not.toHaveProperty("name");
        expect(validationFields).not.toHaveProperty("email");

        expect(localData).toEqual(data);
    })

    it("should set validation errors on dirty by onFieldChange", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        const wrapper = shallow(<ValidationForm onDataChange={onDataChange} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { validationFields, onFieldChange } = childrenFunc.mock.calls[0][0];
        onFieldChange("name", "");
        wrapper.update();
        ({ validationFields, onFieldChange } = childrenFunc.mock.calls[1][0]);
        expect(validationFields).toHaveProperty("extra");
        expect(validationFields).toHaveProperty("name");
        expect(validationFields).not.toHaveProperty("email");
    })

    it("should set validation errors on dirty by onFieldsChange", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        const wrapper = shallow(<ValidationForm onDataChange={onDataChange} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { validationFields, onFieldsChange } = childrenFunc.mock.calls[0][0];
        onFieldsChange({ name: "", email: "" });
        wrapper.update();
        ({ validationFields, onFieldsChange } = childrenFunc.mock.calls[1][0]);
        expect(validationFields).toHaveProperty("extra");
        expect(validationFields).toHaveProperty("name");
        expect(validationFields).toHaveProperty("email");
    })

    it("call isValid force to check constraints all", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        const wrapper = shallow(<ValidationForm onDataChange={onDataChange} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { validationFields, isValid } = childrenFunc.mock.calls[0][0];
        isValid();
        wrapper.update();
        ({ validationFields } = childrenFunc.mock.calls[1][0]);
        expect(validationFields).toHaveProperty("extra");
        expect(validationFields).toHaveProperty("name");
        expect(validationFields).toHaveProperty("email");
    })

    it("should merge with serverValidationError", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        const serverValidationError = {
            message: "Any error",
            errorFields: [
                { name: "name", message: "name error" },
                { name: "email", message: "email error" },
            ]
        }
        shallow(<ValidationForm onDataChange={onDataChange} serverValidationError={serverValidationError} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { validationFields, validationMessage } = childrenFunc.mock.calls[0][0];

        expect(validationFields.name).toEqual("name error");
        expect(validationFields.email).toEqual("email error");
        expect(validationMessage).toEqual("Any error");
    })

    it("should raise onDataChange with onFieldChange", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        shallow(<ValidationForm onDataChange={onDataChange} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { onFieldChange } = childrenFunc.mock.calls[0][0];
        onFieldChange("name", "name 1");
        expect(onDataChange).toHaveBeenCalledWith({
            name: "name 1",
            email: "",
            pass: "",
        });
    })

    it("should raise onDataChange with onFieldsChange", () => {
        const onDataChange = jest.fn();
        const childrenFunc = jest.fn();
        shallow(<ValidationForm onDataChange={onDataChange} classes={{}} data={data} constraints={constraints} >{childrenFunc}</ValidationForm>);
        let { onFieldsChange } = childrenFunc.mock.calls[0][0];
        onFieldsChange({ name: "name 1", email: "email 1" });
        expect(onDataChange).toHaveBeenCalledWith({
            name: "name 1",
            email: "email 1",
            pass: "",
        });
    })
})