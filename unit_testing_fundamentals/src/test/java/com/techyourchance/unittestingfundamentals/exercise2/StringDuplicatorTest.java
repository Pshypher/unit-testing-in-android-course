package com.techyourchance.unittestingfundamentals.exercise2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {
    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String actual = SUT.duplicate("");
        assertThat(actual, is(""));
    }

    @Test
    public void duplicate_singleCharacter_duplicateSimilarCharactersReturned() {
        String actual = SUT.duplicate("a");
        assertThat(actual, is("aa"));
    }

    @Test
    public void duplicate_multipleCharacters_duplicateCopiesInputStringReturned() {
        String actual = SUT.duplicate("vignette");
        assertThat(actual, is("vignettevignette"));

    }
}