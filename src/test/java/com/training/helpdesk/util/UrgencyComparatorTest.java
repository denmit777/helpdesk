package com.training.helpdesk.util;

import com.training.helpdesk.model.enums.Urgency;
import com.training.helpdesk.util.comparator.UrgencyComparator;
import org.junit.Assert;
import org.junit.Test;

public class UrgencyComparatorTest {

    private final UrgencyComparator comparator = new UrgencyComparator();

    @Test
    public void testGetOrderOfUrgency() {
        int expected = 4;

        int actual = comparator.getOrderOfUrgency(Urgency.LOW);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderOfUrgency1() {
        int expected = 3;

        int actual = comparator.getOrderOfUrgency(Urgency.HIGH);

        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void testCompare() {
        int expected = 1;

        int actual = comparator.compare(Urgency.LOW, Urgency.AVERAGE);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompare2() {
        int expected = -3;

        int actual = comparator.compare(Urgency.CRITICAL, Urgency.LOW);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompareIfStatusesAreEquals() {
        int expected = 0;

        int actual = comparator.compare(Urgency.LOW, Urgency.LOW);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareIfStatusesAreEquals1() {
        comparator.compare(Urgency.LOW, Urgency.valueOf("UNKNOWN"));
    }

    @Test
    public void testCompareIfStatusesAreEquals2() {
        int order1 = comparator.getOrderOfUrgency(Urgency.LOW);
        int order2 = comparator.getOrderOfUrgency(Urgency.AVERAGE);

        Assert.assertTrue(order1 > order2);
    }

    @Test
    public void testCompareIfStatusesAreEquals3() {
        int order1 = comparator.getOrderOfUrgency(Urgency.LOW);
        int order2 = comparator.getOrderOfUrgency(Urgency.AVERAGE);

        Assert.assertFalse(order1 < order2);
    }
}
