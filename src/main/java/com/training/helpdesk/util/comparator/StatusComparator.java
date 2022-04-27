package com.training.helpdesk.util.comparator;

import com.training.helpdesk.model.enums.State;

import java.util.Comparator;

public class StatusComparator implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
