package com.mealtracker.services.meal;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
public class DeleteMealsInput {

    @NotNull
    @Size(min = 1, max = 50)
    private List<Long> ids;

    public List<Long> getIds() {
        return ids == null ? Collections.emptyList() : ids;
    }
}
