package com.auroali.sanguinisluxuria.common.components;

/**
 * Used for blood component implementations that require initializing the blood values after construction
 * @see com.auroali.sanguinisluxuria.common.components.impl.EntityBloodComponent
 */
public interface InitializableBloodComponent extends BloodComponent {
    void initializeBloodValues();
}
