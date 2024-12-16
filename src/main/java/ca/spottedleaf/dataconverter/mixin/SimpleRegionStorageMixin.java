package ca.spottedleaf.dataconverter.mixin;

import ca.spottedleaf.dataconverter.minecraft.MCDataConverter;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCDataType;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import com.mojang.serialization.Dynamic;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleRegionStorage.class)
abstract class SimpleRegionStorageMixin implements AutoCloseable {

    @Shadow
    @Final
    private DataFixTypes dataFixType;

    @Unique
    private MCDataType getDataConverterType() {
        if (this.dataFixType == DataFixTypes.ENTITY_CHUNK) {
            return MCTypeRegistry.ENTITY_CHUNK;
        } else if (this.dataFixType == DataFixTypes.POI_CHUNK) {
            return MCTypeRegistry.POI_CHUNK;
        } else {
            throw new UnsupportedOperationException("For " + this.dataFixType.name());
        }
    }

    /**
     * @reason DFU is slow :(
     * @author Spottedleaf
     */
    @Overwrite
    public CompoundTag upgradeChunkTag(final CompoundTag nbt, final int dflVer) {
        final int dataVer = NbtUtils.getDataVersion(nbt, dflVer);
        return MCDataConverter.convertTag(this.getDataConverterType(), nbt, dataVer, SharedConstants.getCurrentVersion().getDataVersion().getVersion());
    }

    /**
     * @reason DFU is slow :(
     * @author Spottedleaf
     */
    @Overwrite
    public Dynamic<Tag> upgradeChunkTag(final Dynamic<Tag> nbt, final int dataVer) {
        final CompoundTag converted = MCDataConverter.convertTag(this.getDataConverterType(), (CompoundTag)nbt.getValue(), dataVer, SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        return new Dynamic<>(nbt.getOps(), converted);
    }
}
