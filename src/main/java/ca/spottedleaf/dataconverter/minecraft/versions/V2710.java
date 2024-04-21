package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.stats.ConverterAbstractStatsRename;

import java.util.Map;

public final class V2710 {

    protected static final int VERSION = MCVersions.V21W15A + 1;

    public static void register() {
        ConverterAbstractStatsRename.register(VERSION, Map.of(
                "minecraft:play_one_minute", "minecraft:play_time"
        )::get);
    }

}
