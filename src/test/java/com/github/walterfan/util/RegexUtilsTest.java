package com.github.walterfan.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegexUtilsTest {

    @Test
    public void isMatched() {
        String email = "walter.hello.world@gmail.com";
        String regex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        assertTrue(email + " should match " + regex, RegexUtils.isMatched(email, regex));
    }
}