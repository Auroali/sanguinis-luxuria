package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VampireAbilityContainer implements Iterable<VampireAbility> {
    private final Set<VampireAbility> abilities;
    private final Map<VampireAbility, AbilityCooldown> cooldowns;
    private final VampireAbility[] abilityBindings = new VampireAbility[3];
    @SuppressWarnings("rawtypes")
    private final Object2ObjectOpenHashMap<VampireAbility, VampireAbility.AbilityTicker> tickers = new Object2ObjectOpenHashMap<>();
    private boolean shouldSync = true;

    public VampireAbilityContainer() {
        abilities = new ObjectOpenHashSet<>();
        cooldowns = new Object2ObjectOpenHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void tick(LivingEntity entity, VampireComponent vampire) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        tickers.object2ObjectEntrySet().fastForEach(p -> p.getValue().tick(p.getKey(), entity.getWorld(), entity, vampire, this, blood));
        cooldowns.entrySet().removeIf(e -> {
            setShouldSync(true);
            return e.getKey().canTickCooldown(entity, vampire) && e.getValue().ticks-- == 0;
        });
    }

    public void addAbility(VampireAbility ability) {
        setShouldSync(true);
        this.abilities.add(ability);
        VampireAbility.AbilityTicker<?> ticker = ability.createTicker();
        if (ticker != null)
            tickers.put(ability, ticker);
    }

    public void removeAbility(VampireAbility ability) {
        setShouldSync(true);
        for (int i = 0; i < abilityBindings.length; i++) {
            if (abilityBindings[i] == ability)
                abilityBindings[i] = null;
        }
        cooldowns.remove(ability);
        this.abilities.remove(ability);
        tickers.remove(ability);
    }

    public VampireAbility getBoundAbility(int slot) {
        if (slot < 0 || abilityBindings.length <= slot)
            return null;
        setShouldSync(true);
        return abilityBindings[slot];
    }

    public void setBoundAbility(VampireAbility ability, int slot) {
        if (!hasAbility(ability) || slot < 0 || abilityBindings.length <= slot)
            return;
        for (int i = 0; i < abilityBindings.length; i++) {
            if (abilityBindings[i] == ability)
                abilityBindings[i] = null;
        }
        setShouldSync(true);
        abilityBindings[slot] = ability;
    }

    public int getAbilityBinding(VampireAbility ability) {
        for (int i = 0; i < abilityBindings.length; i++) {
            if (abilityBindings[i] == ability)
                return i;
        }
        return -1;
    }


    public void setCooldown(VampireAbility ability, int cooldown) {
        if (!hasAbility(ability))
            return;

        setShouldSync(true);
        cooldowns.put(ability, new AbilityCooldown(cooldown));
    }

    public int getCooldown(VampireAbility ability) {
        AbilityCooldown cooldown = cooldowns.get(ability);
        if (cooldown == null)
            return 0;
        return cooldown.ticks;
    }

    public int getMaxCooldown(VampireAbility ability) {
        AbilityCooldown cooldown = cooldowns.get(ability);
        if (cooldown == null)
            return 0;
        return cooldown.maxTicks;
    }

    public boolean isOnCooldown(VampireAbility ability) {
        return getCooldown(ability) > 0;
    }

    public boolean hasAbility(VampireAbility ability) {
        if (ability == null)
            return true;
        return this.abilities.contains(ability);
    }

    public boolean hasAbilityIn(TagKey<VampireAbility> tag) {
        return this.abilities.stream().anyMatch(a -> a.isIn(tag));
    }

    public List<VampireAbility> getAbilitiesIn(TagKey<VampireAbility> tag) {
        return this.abilities.stream().filter(a -> a.isIn(tag)).toList();
    }

    public void save(NbtCompound compound) {
        NbtList abilityTag = new NbtList();
        NbtList abilitySlotsTag = new NbtList();
        NbtList cooldownsTag = new NbtList();
        for (VampireAbility ability : abilities) {
            Identifier id = BLRegistries.VAMPIRE_ABILITIES.getId(ability);
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not find id for an ability!");
                continue;
            }
            abilityTag.add(NbtString.of(id.toString()));
        }
        for (VampireAbility ability : abilityBindings) {
            if (ability == null) {
                abilitySlotsTag.add(NbtString.of("empty"));
                continue;
            }
            Identifier id = BLRegistries.VAMPIRE_ABILITIES.getId(ability);
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not find id for an ability!");
                continue;
            }
            abilitySlotsTag.add(NbtString.of(id.toString()));
        }

        cooldowns.forEach((ability, cooldown) -> {
            Identifier id = BLRegistries.VAMPIRE_ABILITIES.getId(ability);
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not find id for an ability!");
                return;
            }
            NbtCompound tag = new NbtCompound();
            tag.putString("Ability", id.toString());
            tag.putInt("Ticks", cooldown.ticks);
            tag.putInt("MaxTicks", cooldown.maxTicks);
            cooldownsTag.add(tag);
        });

        compound.put("VampireAbilities", abilityTag);
        compound.put("Cooldowns", cooldownsTag);
        compound.put("BoundAbilities", abilitySlotsTag);
    }

    public void load(NbtCompound compound) {
        abilities.clear();
        NbtList abilityTag = compound.getList("VampireAbilities", NbtElement.STRING_TYPE);
        NbtList abilitySlotsTag = compound.getList("BoundAbilities", NbtElement.STRING_TYPE);
        NbtList cooldownsTag = compound.getList("Cooldowns", NbtElement.COMPOUND_TYPE);
        abilityTag.stream()
          .map(NbtString.class::cast)
          .forEach(s -> {
              Identifier id = Identifier.tryParse(s.asString());
              if (id == null) {
                  Bloodlust.LOGGER.warn("Could not get ability for {}", s.asString());
                  return;
              }

              VampireAbility ability = BLRegistries.VAMPIRE_ABILITIES.get(id);
              if (ability == null) {
                  Bloodlust.LOGGER.warn("Could not get ability for {}", id);
                  return;
              }

              abilities.add(ability);
              VampireAbility.AbilityTicker<?> ticker = ability.createTicker();
              if (ticker != null)
                  tickers.put(ability, ticker);
          });
        for (int i = 0; i < Math.min(abilitySlotsTag.size(), abilityBindings.length); i++) {
            String idStr = abilitySlotsTag.getString(i);
            if (idStr.equals("empty"))
                continue;

            Identifier id = Identifier.tryParse(idStr);
            if (id == null) {
                Bloodlust.LOGGER.warn("Could not get ability for {}", abilitySlotsTag.getString(i));
                continue;
            }

            VampireAbility ability = BLRegistries.VAMPIRE_ABILITIES.get(id);
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not get ability for {}", id);
                continue;
            }

            abilityBindings[i] = ability;
        }
        cooldowns.clear();
        for (int i = 0; i < cooldownsTag.size(); i++) {
            NbtCompound cooldown = cooldownsTag.getCompound(i);
            Identifier id = Identifier.tryParse(cooldown.getString("Ability"));
            int ticks = cooldown.getInt("Ticks");
            int maxTicks = cooldown.getInt("MaxTicks");

            if (id == null) {
                Bloodlust.LOGGER.warn("Could not get ability for {}", abilitySlotsTag.getString(i));
                continue;
            }

            VampireAbility ability = BLRegistries.VAMPIRE_ABILITIES.get(id);
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not get ability for {}", id);
                continue;
            }

            cooldowns.put(ability, new AbilityCooldown(ticks, maxTicks));
        }
        setShouldSync(true);
    }

    public void writePacket(PacketByteBuf buf) {
        // write unlocked abilities
        buf.writeInt(abilities.size());
        for (VampireAbility ability : abilities) {
            buf.writeRegistryValue(BLRegistries.VAMPIRE_ABILITIES, ability);
        }

        // write bound abilities
        int abilities = 0;
        for (VampireAbility a : abilityBindings) {
            if (a != null)
                abilities++;
        }
        buf.writeVarInt(abilities);
        for (int i = 0; i < abilityBindings.length; i++) {
            if (abilityBindings[i] == null)
                continue;

            buf.writeVarInt(i);
            buf.writeRegistryValue(BLRegistries.VAMPIRE_ABILITIES, abilityBindings[i]);
        }

        // write cooldowns
        buf.writeVarInt(cooldowns.size());
        cooldowns.forEach((ability, cooldown) -> {
            buf.writeRegistryValue(BLRegistries.VAMPIRE_ABILITIES, ability);
            buf.writeVarInt(cooldown.ticks);
            buf.writeVarInt(cooldown.maxTicks);
        });
    }

    public void readPacket(PacketByteBuf buf) {
        // read unlocked abilities
        abilities.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            VampireAbility ability = buf.readRegistryValue(BLRegistries.VAMPIRE_ABILITIES);
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not read ability from packet!");
                continue;
            }

            abilities.add(ability);
        }

        // read bound abilities
        size = buf.readVarInt();
        Arrays.fill(abilityBindings, null);
        for (int i = 0; i < size; i++) {
            int slot = buf.readVarInt();
            VampireAbility ability = buf.readRegistryValue(BLRegistries.VAMPIRE_ABILITIES);
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not read ability from packet!");
                continue;
            }

            abilityBindings[slot] = ability;
        }

        // read cooldowns
        size = buf.readVarInt();
        cooldowns.clear();
        for (int i = 0; i < size; i++) {
            VampireAbility ability = buf.readRegistryValue(BLRegistries.VAMPIRE_ABILITIES);
            int ticks = buf.readVarInt();
            int maxTicks = buf.readVarInt();
            if (ability == null) {
                Bloodlust.LOGGER.warn("Could not read ability from packet!");
                continue;
            }
            cooldowns.put(ability, new AbilityCooldown(ticks, maxTicks));
        }
    }

    public boolean needsSync() {
        return shouldSync;
    }

    public void setShouldSync(boolean shouldSync) {
        this.shouldSync = shouldSync;
    }

    @NotNull
    @Override
    public Iterator<VampireAbility> iterator() {
        return abilities.iterator();
    }

    public void clearBoundAbility(VampireAbility ability) {
        int binding = getAbilityBinding(ability);
        if (binding != -1)
            setBoundAbility(null, binding);
    }

    private static class AbilityCooldown {
        int ticks;
        int maxTicks;

        public AbilityCooldown(int ticks) {
            this(ticks, ticks);
        }

        public AbilityCooldown(int ticks, int maxTicks) {
            this.ticks = ticks;
            this.maxTicks = maxTicks;
        }
    }
}
