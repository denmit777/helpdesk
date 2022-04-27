package com.training.helpdesk.util.comparator;

import com.training.helpdesk.exception.WrongSortOrderException;
import com.training.helpdesk.model.enums.Urgency;

import java.util.*;

public class UrgencyComparator implements Comparator<Urgency> {

    private static final Map<String, Integer> URGENCY_MAP = new HashMap<>();
    private static final Set<Map.Entry<String, Integer>> URGENCY_SET = URGENCY_MAP.entrySet();

    public int getOrderOfUrgency(Urgency urgency) {
        URGENCY_MAP.put("CRITICAL", 1);
        URGENCY_MAP.put("HIGH", 2);
        URGENCY_MAP.put("AVERAGE", 3);
        URGENCY_MAP.put("LOW", 4);

        return URGENCY_SET.stream()
                .filter(pair -> pair.getKey().equals(urgency.name()))
                .map(Map.Entry::getValue)
                .mapToInt(Integer::intValue).findFirst().orElseThrow(() -> new WrongSortOrderException("Wrong order"));
    }

    @Override
    public int compare(Urgency o1, Urgency o2) {
        int urgency1 = getOrderOfUrgency(o1);
        int urgency2 = getOrderOfUrgency(o2);

        return urgency1 - urgency2;
    }
}
