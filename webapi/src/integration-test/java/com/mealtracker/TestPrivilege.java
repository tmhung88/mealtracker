package com.mealtracker;

import com.mealtracker.domains.Privilege;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPrivilege {

    public static List<Privilege> exclude(Privilege... excludedPrivileges) {
        var allPrivileges = new ArrayList<>(Arrays.asList(Privilege.values()));
        allPrivileges.removeAll(Arrays.asList(excludedPrivileges));
        return allPrivileges;
    }
}
