package com.almostreliable.appelem.core;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AppElemConfig {

    public static final ModConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        var commonPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = commonPair.getRight();
        COMMON = commonPair.getLeft();
    }

    private AppElemConfig() {}

    public static final class CommonConfig {

        public final ModConfigSpec.IntValue containerCapacity;

        private CommonConfig(ModConfigSpec.Builder builder) {
            containerCapacity = builder.comment("The element capacity of the ME Element Container.")
                .defineInRange("container_capacity", 100_000, 0, 100_000_000);
        }
    }
}
