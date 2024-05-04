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

        public final ModConfigSpec.IntValue amountPerOperation;
        public final ModConfigSpec.IntValue containerCapacity;

        private CommonConfig(ModConfigSpec.Builder builder) {
            amountPerOperation = builder.comment("The amount of element handled per operation, e.g. when inserting or extracting.")
                .defineInRange("amount_per_operation", 100, 1, 10_000);
            containerCapacity = builder.comment("The element capacity of the ME Element Container.")
                .defineInRange("container_capacity", 100_000, 1, 100_000_000);
        }
    }
}
