package net.boston.mythicarmor.block.entity;

import net.boston.mythicarmor.gui.ImbuingStationMenu;
import net.boston.mythicarmor.item.ModItems;
import net.boston.mythicarmor.item.custom.EssenceItem;
import net.boston.mythicarmor.item.custom.MythicItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ImbuingStationBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 10;


    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    public ImbuingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IMBUING_STATION.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ImbuingStationBlockEntity.this.progress;
                    case 1 -> ImbuingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> ImbuingStationBlockEntity.this.progress = pValue;
                    case 1 -> ImbuingStationBlockEntity.this.progress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Imbuing Station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ImbuingStationMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("imbuing_station.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("imbuing_station.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ImbuingStationBlockEntity pEntity) {
        if (level.isClientSide())
            return;

        if (hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, blockPos, blockState);

            if (pEntity.progress >= pEntity.maxProgress)
                imbueItem(pEntity);
        } else {
            pEntity.resetProgress();
            setChanged(level, blockPos, blockState);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void imbueItem(ImbuingStationBlockEntity pEntity) {
        if (hasRecipe(pEntity)) {
            pEntity.progress++;

            if (pEntity.progress >= pEntity.maxProgress) {
                @NotNull Item essence = pEntity.itemHandler.getStackInSlot(0).getItem();
                @NotNull ItemStack mythicItem = pEntity.itemHandler.getStackInSlot(1);

                MythicItem.imbue(mythicItem, (EssenceItem)essence);

                pEntity.itemHandler.extractItem(0, 1, false);

                pEntity.getLevel().playSound(null, pEntity.getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.3f, 0.6f);

                pEntity.resetProgress();
            }
        }
    }

    private static boolean hasRecipe(ImbuingStationBlockEntity pEntity) {
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        // Is there essence in the entity
        Item itemInSlot1 = pEntity.itemHandler.getStackInSlot(0).getItem();
        boolean hasEssence = Arrays.stream(ModItems.ESSENCES_ARR).anyMatch((item) -> item.get() == itemInSlot1);

        // Is there a mythic item in the input slot
        Item itemInSlot2 = inventory.getItem(1).getItem();
        boolean hasArmor = Arrays.stream(ModItems.MYTHIC_ITEMS_ARR).anyMatch((item) -> item.get() == itemInSlot2);

        // Only do the rest of the checks if it has already been confirmed that this is a mythic armor item
        if (!hasArmor) return false;

        // Can the armor be imbued
        boolean canBeImbued = MythicItem.canImbueAmount(inventory.getItem(1), 1) && MythicItem.isValidImbue(inventory.getItem(0), inventory.getItem(1));

        return hasEssence && canBeImbued; // note hasArmor has already been used for previous return
    }
}
