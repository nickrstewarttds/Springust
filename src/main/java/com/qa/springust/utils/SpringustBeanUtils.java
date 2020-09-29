package com.qa.springust.utils;

import org.springframework.beans.BeanUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SpringustBeanUtils {

    public static void mergeObject(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

}
