package com.auroali.sanguinisluxuria.common.components;

/**
 * Used for blood component implementations that require initializing the blood values after construction
 *
 * @see com.auroali.sanguinisluxuria.common.components.impl.EntityBloodComponent
 */
public interface InitializableBloodComponent extends BloodComponent {
    /**
     * Initializes this component's blood values
     *
     * @param sync whether to sync to the client
     */
    void initializeBloodValues(boolean sync);

    /**
     * Initializes this component's blood values, and performs
     * a sync if possible
     */
    void initializeBloodValues();

    /**
     * Checks if this component needs to be initialized
     *
     * @return if this component needs to be initialized (e.g. blood values have either
     * not been set up or do not match expected values)
     */
    boolean hasInitialized();
}
