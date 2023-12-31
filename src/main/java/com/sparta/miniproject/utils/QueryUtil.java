package com.sparta.miniproject.utils;

import com.querydsl.core.types.Predicate;
import com.sparta.miniproject.entity.QCompany;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class QueryUtil {
    public Predicate companyNameContains(QCompany company, String keyword) {
        return StringUtils.hasText(keyword) ? company.companyName.containsIgnoreCase(keyword) : null;
    }
}
