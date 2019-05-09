import React from "react";
import { shallow } from "enzyme";
import { UserSelect } from "../UserSelect";
import AsyncSelect from "react-select/lib/Async";

describe("#UserSelect", () => {
    it("should fetch data on searching", async () => {
        const data = [
            { id: "id1", email: "email1" },
        ]
        const api = {
            get: jest.fn().mockResolvedValue({ json: jest.fn().mockResolvedValue({ data: data }) })
        }
        const wrapper = shallow(<UserSelect classes={{}} api={api} />);
        const result = await wrapper.find(AsyncSelect).prop("loadOptions")("input-value");

        expect(api.get).toHaveBeenCalledWith("/v1/users?keyword=input-value");
        expect(result).toEqual([{ key: "id1", label: "email1" }]);
    })

    it("should raise change event", ()=>{
        const onUserChange = jest.fn();
        const wrapper = shallow(<UserSelect classes={{}} onUserChange={onUserChange} />);
        wrapper.find(AsyncSelect).simulate("change","value1");

        expect(onUserChange).toHaveBeenCalledWith("value1");
    })
})
