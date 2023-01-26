package me.cosm1x.unomod.eventhandler;

public class EventsRegistry {
    public static void register() {
        TickEvents.register();
        BlockBreakEvent.register();
        UseBlockEvent.register();
        UseItemEvent.register();
        // OnRepawnEvent.register();
        // ConnectionEvents.register();
    }
}
