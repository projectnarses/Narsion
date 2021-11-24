package org.narses.narsion.quest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ConstructedQuest implements Quest {

    private final @NotNull List<QuestStep> steps = new ArrayList<>();
    private QuestStep[] stepsArray;

    protected ConstructedQuest() {
    }

    @Override
    public @NotNull QuestStep @NotNull [] steps() {
        if (stepsArray == null) {
            stepsArray = steps.toArray(QuestStep[]::new);
        }
        return stepsArray;
    }

    protected void addStep(QuestStep step) {
        steps.add(step);
    }
}
