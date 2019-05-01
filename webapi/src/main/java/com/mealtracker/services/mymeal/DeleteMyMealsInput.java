package com.mealtracker.services.mymeal;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
public class DeleteMyMealsInput {

    @NotNull
    @Size(min = 1, max = 50)
    private List<Long> mealIds;

    public List<Long> getMealIds() {
        return mealIds == null ? Collections.emptyList() : mealIds;
    }
}
