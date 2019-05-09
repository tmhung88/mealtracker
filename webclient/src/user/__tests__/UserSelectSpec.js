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

        expect(api.get).toHaveBeenCalledWith("/v1/users/select?search=input-value");
        expect(result).toEqual([{ key: "id1", label: "email1" }]);
    })
})