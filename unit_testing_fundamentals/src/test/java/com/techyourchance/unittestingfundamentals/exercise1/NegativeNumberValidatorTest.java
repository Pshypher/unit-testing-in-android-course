package com.techyourchance.unittestingfundamentals.exercise1;

import static org.hamcrest.core.Is.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void test1() {
        boolean actual = SUT.isNegative(-1);
        Assert.assertThat(actual, is(true));
    }

    @Test
    public void test2() {
        boolean actual = SUT.isNegative(0);
        Assert.assertThat(actual, is(false));
    }

    @Test
    public void test3() {
        boolean actual = SUT.isNegative(1);
        Assert.assertThat(actual, is(false));
    }

}