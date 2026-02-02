package net.blumasc.selectivepowers.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEnchantmentStateHandler {

    private static final String NBT_KEY = "selectivepowers:enchantment_state";

    public static void saveState(LivingEntity player, PlayerEnchantmentState state) {
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag stateTag = new CompoundTag();

        stateTag.putInt("cloudStepJumpsUsed", state.getCloudStepJumpsUsed());
        stateTag.putBoolean("dashUsed", state.isDashUsed());
        stateTag.putBoolean("justJumped", state.hasJustJumped());
        if (state.getFrenzyLastAttackedEntity() != null)
            stateTag.putUUID("frenzyLastAttackedEntity", state.getFrenzyLastAttackedEntity());

        CompoundTag cooldownsTag = new CompoundTag();
        for (Map.Entry<ResourceLocation, Long> entry : state.cooldowns.entrySet()) {
            cooldownsTag.putLong(entry.getKey().toString(), entry.getValue());
        }
        stateTag.put("cooldowns", cooldownsTag);

        tag.put(NBT_KEY, stateTag);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
    }

    public static PlayerEnchantmentState loadState(LivingEntity player) {
        PlayerEnchantmentState state = new PlayerEnchantmentState();
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        if (!tag.contains(NBT_KEY)) return state;

        CompoundTag stateTag = tag.getCompound(NBT_KEY);

        state.resetCloudStepJumps();
        if(stateTag.contains("cloudStepJumpsUsed")) {
            state.cloudStepJumpsUsed = stateTag.getInt("cloudStepJumpsUsed");
        }
        if(stateTag.contains("dashUsed")) {
            state.setDashUsed(stateTag.getBoolean("dashUsed"));
        }
        if(stateTag.contains("justJumped")) {
            state.setJustJumped(stateTag.getBoolean("justJumped"));
        }
        if (stateTag.hasUUID("frenzyLastAttackedEntity"))
            state.setFrenzyLastAttackedEntity(stateTag.getUUID("frenzyLastAttackedEntity"));

        CompoundTag cooldownsTag = stateTag.getCompound("cooldowns");
        for (String key : cooldownsTag.getAllKeys()) {
            ResourceLocation id = ResourceLocation.tryParse(key);
            if (id != null)
                state.setCooldown(id, cooldownsTag.getLong(key));
        }

        return state;
    }

}

