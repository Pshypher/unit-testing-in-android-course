package com.techyourchance.unittestingfundamentals.exercise3;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    // interval 1 before and not adjacent to interval 2
    @Test
    public void isAdjacent_interval1BeforeAndNotAdjacentToInterval2_falseReturned() {
        Interval a = new Interval(-4, 2);
        Interval b = new Interval(4, 10);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 before and adjacent to interval 2
    @Test
    public void isAdjacent_interval1BeforeAndAdjacentToInterval2_trueReturned() {
        Interval a = new Interval(-4, 2);
        Interval b = new Interval(2, 6);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(true));
    }

    // interval 1 overlaps at start of interval 2
    @Test
    public void isAdjacent_interval1OverlapsAtStartOfInterval2_falseReturned() {
        Interval a = new Interval(-4, 4);
        Interval b = new Interval(0, 12);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 is contained by interval 2
    @Test
    public void isAdjacent_interval1ContainedByInterval2_falseReturned() {
        Interval a = new Interval(-2, 2);
        Interval b = new Interval(-4, 6);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 contains interval 2
    @Test
    public void isAdjacent_interval1ContainsInterval2_falseReturned() {
        Interval a = new Interval(0, 15);
        Interval b = new Interval(6, 10);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 overlaps at end of interval 2
    @Test
    public void isAdjacent_interval1OverlapsAtEndOfInterval2_falseReturned() {
        Interval a = new Interval(-5, 10);
        Interval b = new Interval(-9, -3);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 after and not adjacent to interval 2
    @Test
    public void isAdjacent_interval1AfterNotAdjacentToInterval2_falseReturned() {
        Interval a = new Interval(10, 15);
        Interval b = new Interval(-4, 5);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(false));
    }

    // interval 1 after and adjacent to interval 2
    @Test
    public void isAdjacent_interval1AfterAdjacentToInterval2_trueReturned() {
        Interval a = new Interval(5, 8);
        Interval b = new Interval(-2, 5);
        boolean actual = SUT.isAdjacent(a, b);
        assertThat(actual, is(true));
    }

    // interval 1 same as interval 2
    @Test
    public void isAdjacent_interval1SameAsInterval2_falseReturned() {
        Interval interval = new Interval(-4, 2);
        Interval interval2 = new Interval(-4, 2);
        boolean actual = SUT.isAdjacent(interval, interval2);
        assertThat(actual, is(false));
    }
}