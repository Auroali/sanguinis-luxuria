package com.auroali.sanguinisluxuria.common.abilities;

import java.util.Collection;

public class DefaultedVampireAbilityContainer extends VampireAbilityContainer {
    public DefaultedVampireAbilityContainer(Collection<VampireAbility> abilities, Runnable syncCallback) {
        super(syncCallback);
        abilities.forEach(a -> this.abilities.put(a, new VampireAbilityContainer.AbilityEntry(a)));
    }
}
