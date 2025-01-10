package com.exosomnia.exoarmory.actions;


import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private static int TICK_COUNT = 0;
    private final Map<Integer, List<Action>> ACTION_SCHEDULE = new HashMap<>();

    public void tick() {
        List<Action> scheduled = ACTION_SCHEDULE.get(TICK_COUNT);
        if (scheduled != null) {
            scheduled.forEach(Action::execute);
            ACTION_SCHEDULE.remove(TICK_COUNT);
        }
        TICK_COUNT++;
    }

    public void scheduleAction (Action action, int ticks) {
        List<Action> scheduled = ACTION_SCHEDULE.putIfAbsent(TICK_COUNT + ticks, new ArrayList<>());
        scheduled = scheduled == null ? ACTION_SCHEDULE.get(TICK_COUNT + ticks) : scheduled;
        scheduled.add(action);
    }
}
