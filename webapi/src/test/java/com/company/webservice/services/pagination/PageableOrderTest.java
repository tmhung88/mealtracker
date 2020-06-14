package com.company.webservice.services.pagination;

import com.company.webservice.services.pagination.PageableOrder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.data.domain.Sort;

public class PageableOrderTest {

    @Test
    public void of_asc_ExpectSpringAscOrderReturned() {
        Assertions.assertThat(PageableOrder.ASC.of("id")).isEqualTo(Sort.Order.asc("id"));
    }

    @Test
    public void of_desc_ExpectSpringDescOrderReturned() {
        Assertions.assertThat(PageableOrder.DESC.of("name")).isEqualTo(Sort.Order.desc("name"));
    }
}
