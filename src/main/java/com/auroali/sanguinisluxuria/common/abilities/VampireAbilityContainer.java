package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class VampireAbilityContainer implements Iterable<Map.Entry<VampireAbility, VampireAbilityContainer.AbilityEntry>> {
    private Map<VampireAbility, AbilityEntry> abilities;
    private boolean shouldSync = true;

    public VampireAbilityContainer() {
        this.abilities = new Object2ObjectOpenHashMap<>();
    }

    public void tick(LivingEntity entity, VampireComponent vampire) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        this.abilities.values().forEach(entry -> entry.tick(entity, vampire, blood));
    }

    public void addAbility(VampireAbility ability) {
        this.abilities.put(ability, new AbilityEntry(ability));
        this.setShouldSync(true);
    }

    public void removeAbility(VampireAbility ability) {
        this.abilities.remove(ability);
        this.setShouldSync(true);
    }

    public AbilityEntry getAbility(VampireAbility ability) {
        return this.abilities.get(ability);
    }

    public boolean hasAbility(VampireAbility ability) {
        if (ability == null)
            return false;
        return this.abilities.containsKey(ability);
    }

    public void save(NbtCompound compound) {
        NbtList abilities = new NbtList();
        this.abilities.values().forEach(entry -> {
            NbtCompound tag = new NbtCompound();
            entry.writeNbt(tag);
            abilities.add(tag);
        });

        compound.put("Abilities", abilities);
    }

    public void load(NbtCompound compound) {
        if (compound.contains("VampireAbilities", NbtElement.LIST_TYPE)) {
            loadLegacy(compound, this);
            return;
        }

        NbtList abilitiesTag = compound.getList("Abilities", NbtElement.COMPOUND_TYPE);
        Object2ObjectOpenHashMap<VampireAbility, AbilityEntry> abilities = new Object2ObjectOpenHashMap<>();
        for (int i = 0; i < abilitiesTag.size(); i++) {
            AbilityEntry entry = AbilityEntry.readNbt(abilitiesTag.getCompound(i), this);
            if (entry == null)
                continue;

            abilities.put(entry.ability, entry);
        }

        this.abilities = abilities;
        this.setShouldSync(true);
    }

    public void writePacket(PacketByteBuf buf) {
        buf.writeVarInt(this.abilities.size());
        this.abilities.values().forEach(entry -> entry.write(buf));
    }

    public void readPacket(PacketByteBuf buf) {
        Object2ObjectOpenHashMap<VampireAbility, AbilityEntry> abilities = new Object2ObjectOpenHashMap<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            AbilityEntry entry = AbilityEntry.read(buf, this);
            abilities.put(entry.ability, entry);
        }

        this.abilities = abilities;
    }

    public boolean needsSync() {
        return this.shouldSync;
    }

    public void setShouldSync(boolean shouldSync) {
        this.shouldSync = shouldSync;
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<VampireAbility, VampireAbilityContainer.AbilityEntry>> iterator() {
        return Iterators.unmodifiableIterator(this.abilities.entrySet().iterator());
    }

    public Collection<VampireAbility> abilities() {
        return Collections.unmodifiableCollection(this.abilities.keySet());
    }

    public Collection<AbilityEntry> entries() {
        return Collections.unmodifiableCollection(this.abilities.values());
    }

    private static void loadLegacy(NbtCompound tag, VampireAbilityContainer container) {
        NbtList abilities = tag.getList("VampireAbilities", NbtElement.STRING_TYPE);
        Object2ObjectOpenHashMap<VampireAbility, AbilityEntry> abilityMap = new Object2ObjectOpenHashMap<>();
        for (int i = 0; i < abilities.size(); i++) {
            Identifier id = Identifier.tryParse(abilities.getString(i));
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not parse id {}", abilities.getString(i));
                continue;
            }

            BLRegistries.VAMPIRE_ABILITIES.getOrEmpty(id)
              .ifPresent(ability -> abilityMap.put(ability, container.new AbilityEntry(ability)));
        }

        container.abilities = abilityMap;
        container.setShouldSync(true);
    }

    public class AbilityEntry {
        private final VampireAbility ability;
        private final VampireAbility.AbilityTicker<VampireAbility> ticker;
        private int cooldownTicks;
        private int maxCooldownTicks;

        @SuppressWarnings("unchecked")
        protected AbilityEntry(VampireAbility ability) {
            this.ability = ability;
            this.ticker = (VampireAbility.AbilityTicker<VampireAbility>) ability.createTicker();
        }

        public void tick(LivingEntity entity, VampireComponent vampire, BloodComponent blood) {
            if (this.ticker != null)
                this.ticker.tick(this.ability, entity.getWorld(), entity, vampire, VampireAbilityContainer.this, blood);

            if (this.cooldownTicks > 0) {
                if (--this.cooldownTicks == 0) {
                    this.ability.onCooldownEnd(entity, vampire, VampireAbilityContainer.this);
                    this.maxCooldownTicks = 0;
                }
                VampireAbilityContainer.this.setShouldSync(true);
            }
        }

        public void setCooldown(int cooldown) {
            this.maxCooldownTicks = cooldown;
            this.cooldownTicks = cooldown;
            VampireAbilityContainer.this.setShouldSync(true);
        }

        public int getCooldown() {
            return this.cooldownTicks;
        }

        public int getMaxCooldown() {
            return this.maxCooldownTicks;
        }

        public void writeNbt(NbtCompound nbt) {
            nbt.putString("id", BLRegistries.VAMPIRE_ABILITIES.getId(this.ability).toString());
            nbt.putInt("Cooldown", this.cooldownTicks);
            nbt.putInt("MaxCooldown", this.maxCooldownTicks);
        }

        public static AbilityEntry readNbt(NbtCompound nbt, VampireAbilityContainer container) {
            Identifier id = Identifier.tryParse(nbt.getString("id"));
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not parse id {}", nbt.getString("id"));
                return null;
            }

            return BLRegistries.VAMPIRE_ABILITIES.getOrEmpty(id)
              .map(ability -> {
                  int cooldown = nbt.getInt("Cooldown");
                  int maxCooldown = nbt.getInt("MaxCooldown");

                  AbilityEntry entry = container.new AbilityEntry(ability);
                  entry.cooldownTicks = cooldown;
                  entry.maxCooldownTicks = maxCooldown;
                  return entry;
              })
              .orElseGet(() -> {
                  Bloodlust.LOGGER.warn("Unknown ability {}", id);
                  return null;
              });
        }

        public void write(PacketByteBuf buf) {
            buf.writeRegistryValue(BLRegistries.VAMPIRE_ABILITIES, this.ability);
            buf.writeVarInt(this.cooldownTicks);
            buf.writeVarInt(this.maxCooldownTicks);
        }

        public static AbilityEntry read(PacketByteBuf buf, VampireAbilityContainer container) {
            VampireAbility ability = buf.readRegistryValue(BLRegistries.VAMPIRE_ABILITIES);
            int cooldown = buf.readVarInt();
            int maxCooldown = buf.readVarInt();
            AbilityEntry entry = container.new AbilityEntry(ability);
            entry.cooldownTicks = cooldown;
            entry.maxCooldownTicks = maxCooldown;
            return entry;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof AbilityEntry entry && entry.ability == this.ability);
        }

        @Override
        public int hashCode() {
            return this.ability.hashCode();
        }

        public boolean isOnCooldown() {
            return this.cooldownTicks > 0 && this.maxCooldownTicks > 0;
        }
    }
}
