package com.company.webservice.services.pagination;

import com.company.webservice.assertions.AppAssertions;
import com.company.webservice.ValidatorProvider;
import org.junit.Test;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleSortableColumnPageableParamsTest {
    private final Validator validator = ValidatorProvider.getValidator();

    @Test
    public void getSortableProperties_ExpectSinglePropertyReturned() {
        var params = validParams();
        params.setOrder("asc");
        assertThat(params.getSortableProperties())
                .containsExactly(new SortableProperty(0, params.getOrderBy(), PageableOrder.ASC));
    }

    @Test
    public void input_RowsPerPageTooSmall_ExpectMinViolation() {
        var params = validParams();
        params.setRowsPerPage(0);

        AppAssertions.assertThat(validator.validate(params)).violateMin("rowsPerPage", 1);
    }

    @Test
    public void input_RowsPerPageTooBig_ExpectMaxViolation() {
        var params = validParams();
        params.setRowsPerPage(51);

        AppAssertions.assertThat(validator.validate(params)).violateMax("rowsPerPage", 50);
    }

    @Test
    public void input_PageIndexNegative_ExpectPositiveOrZeroViolation() {
        var params = validParams();
        params.setPageIndex(-1);

        AppAssertions.assertThat(validator.validate(params)).violatePositiveOrZero("pageIndex");
    }

    @Test
    public void input_OrderUndefinedValue_ExpectValueInListViolation() {
        var params = validParams();
        params.setOrder("DESC");

        AppAssertions.assertThat(validator.validate(params)).violateValueInList("order", "asc", "desc");
    }

    public SingleSortableColumnPageableParams validParams() {
        var params = new SingleSortableColumnPageableParams() {
            @Override
            public String getOrderBy() {
                return "any column";
            }
        };
        params.setPageIndex(5);
        params.setRowsPerPage(20);
        params.setOrder("asc");
        return params;
    }
}
