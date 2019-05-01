package com.mealtracker.services.mymeal;

import lombok.Data;

import java.util.List;

@Data
public class DeleteMyMealsInput {
    private List<Long> mealIds;
}
