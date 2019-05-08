import React from "react";
import { shallow } from "enzyme";
import { UserList } from "../UserList";
import ServerPagingTable from "../../common/table/ServerPagingTable";

describe("#UserList", () => {
    it("should navigate to update page on row select", () => {
        const history = {
            push: jest.fn(),
        }
        const wrapper = shallow(<UserList classes={{}} history={history} />);
        wrapper.find(ServerPagingTable).simulate("rowSelect", "123");
        expect(history.push).toHaveBeenCalledWith("/users/123/update")
    });
})