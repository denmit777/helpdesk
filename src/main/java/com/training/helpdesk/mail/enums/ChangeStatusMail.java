package com.training.helpdesk.mail.enums;

import com.training.helpdesk.model.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.training.helpdesk.model.enums.State.*;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum ChangeStatusMail {
    DRAFT_NEW(Map.of(DRAFT, NEW)),
    DECLINED_NEW(Map.of(DECLINED, NEW)),
    NEW_APPROVED(Map.of(NEW, APPROVED)),
    NEW_DECLINED(Map.of(NEW, DECLINED)),
    NEW_CANCELED(Map.of(NEW, CANCELED)),
    APPROVED_CANCELED(Map.of(APPROVED, CANCELED)),
    IN_PROGRESS_DONE(Map.of(IN_PROGRESS, DONE));

    private final Map<State, State> previousStateToCurrentStateMap;

    public boolean isPreviousStateToCurrentStateMapEqualTo(Map<State, State> other) {
        return previousStateToCurrentStateMap.equals(other);
    }
}

