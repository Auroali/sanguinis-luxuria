package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.Bloodlust;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Set;

public class VampireAbilityContainer {
    private final Set<VampireAbility> abilities;

    public VampireAbilityContainer() {
        abilities = new ObjectOpenHashSet<>();
    }

    public void tick(LivingEntity entity, VampireComponent vampire) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        for(VampireAbility ability : abilities) {
            ability.tick(entity, vampire, blood);
        }
    }

    public void addAbility(VampireAbility ability) {
        this.abilities.add(ability);
    }

    public void removeAbility(VampireAbility ability) {
        this.abilities.remove(ability);
    }

    public boolean hasAbility(VampireAbility ability) {
        return this.abilities.contains(ability);
    }

    public void save(NbtCompound compound) {
        NbtList abilityTag = new NbtList();
        for(VampireAbility ability : abilities) {
            Identifier id = BLRegistry.VAMPIRE_ABILITIES.getId(ability);
            if(id == null) {
                Bloodlust.LOGGER.warn("Could not find id for an ability!");
                continue;
            }
            abilityTag.add(NbtString.of(id.toString()));
        }
        compound.put("VampireAbilities", abilityTag);
    }

    public void load(NbtCompound compound) {
        abilities.clear();
        NbtList abilityTag = compound.getList("VampireAbilities", NbtElement.STRING_TYPE);
        abilityTag.stream()
                .map(NbtString.class::cast)
                .forEach(s -> {
                    Identifier id = Identifier.tryParse(s.asString());
                    if(id == null) {
                        Bloodlust.LOGGER.warn("Could not get ability for {}", s.asString());
                    }

                    VampireAbility ability = BLRegistry.VAMPIRE_ABILITIES.get(id);
                    if(ability == null) {
                        Bloodlust.LOGGER.warn("Could not get ability for {}", id);
                    }

                    abilities.add(ability);
                });
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeInt(abilities.size());
        for(VampireAbility ability : abilities) {
            buf.writeRegistryValue(BLRegistry.VAMPIRE_ABILITIES, ability);
        }
    }

    public void readPacket(PacketByteBuf buf) {
        abilities.clear();
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            VampireAbility ability = buf.readRegistryValue(BLRegistry.VAMPIRE_ABILITIES);
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not read ability from packet!");
                continue;
            }

            abilities.add(ability);
        }
    }
}
