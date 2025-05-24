package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.SLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.google.common.base.Predicates;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.function.Predicate;

public class EntityVampireComponent<T extends LivingEntity> implements VampireComponent {
    private final Predicate<T> vampirePredicate;
    private final T holder;
    private final VampireAbilityContainer abilities;
    private boolean downed;
    private boolean isMist;

    public EntityVampireComponent(T holder, Predicate<T> vampirePredicate, VampireAbility... abilities) {
        this.holder = holder;
        this.vampirePredicate = vampirePredicate;
        this.abilities = new VampireAbilityContainer(Arrays.asList(abilities), () -> VampireComponent.KEY.sync(this.holder));
    }

    public EntityVampireComponent(T holder, VampireAbility... abilities) {
        this(holder, Predicates.alwaysTrue(), abilities);
    }

    @Override
    public boolean isVampire() {
        return this.vampirePredicate.test(this.holder);
    }

    @Override
    public void setVampire(boolean isVampire) {

    }

    @Override
    public VampireAbilityContainer getAbilityContainer() {
        return this.abilities;
    }

    @Override
    public boolean isDowned() {
        return this.downed;
    }

    @Override
    public void setDowned(boolean down) {
        this.downed = down;
        VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public boolean isMist() {
        return this.isMist;
    }

    @Override
    public void setMist(boolean isMist) {
        this.isMist = isMist;
        VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public void serverTick() {
        this.abilities.tick(this.holder, this);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(this.downed);
        buf.writeBoolean(this.isMist);
        this.abilities.writePacket(buf);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.downed = buf.readBoolean();
        this.isMist = buf.readBoolean();
        this.abilities.readPacket(buf);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.downed = tag.getBoolean("Downed");
        this.isMist = tag.getBoolean("IsMist");
        this.abilities.readNbt(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("Downed", this.downed);
        tag.putBoolean("IsMist", this.isMist);
        this.abilities.writeNbt(tag);
    }
}
