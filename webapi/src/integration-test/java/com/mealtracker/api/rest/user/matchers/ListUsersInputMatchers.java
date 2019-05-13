package com.mealtracker.api.rest.user.matchers;

import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.user.ListUsersInput;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class ListUsersInputMatchers {
    public static ListUsersInput pagination(String orderBy, PageableOrder order) {
        return argThat(new PaginationMatcher(0, 1, orderBy, order));
    }

    public static ListUsersInput pagination(int pageIndex, int rowsPerPage, String orderBy, PageableOrder order) {
        return argThat(new PaginationMatcher(pageIndex, rowsPerPage, orderBy, order));
    }

    static class PaginationMatcher implements ArgumentMatcher<ListUsersInput> {

        private final int pageIndex;
        private final int rowsPerPage;
        private final String orderBy;
        private final PageableOrder order;

        public PaginationMatcher(int pageIndex, int rowsPerPage, String orderBy, PageableOrder order) {
            this.pageIndex = pageIndex;
            this.rowsPerPage = rowsPerPage;
            this.orderBy = orderBy;
            this.order = order;
        }

        @Override
        public boolean matches(ListUsersInput actual) {
            return pageIndex == actual.getPageIndex() &&
                    rowsPerPage == actual.getRowsPerPage() &&
                    orderBy.equals(actual.getOrderBy()) &&
                    order.name().toLowerCase().equals(actual.getOrder());
        }
    }
}
