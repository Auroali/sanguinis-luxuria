package com.auroali.sanguinisluxuria.common.network;

import net.minecraft.network.PacketByteBuf;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Handles serializing a provided type to/from a packet,
 * conditionally writing sections using provided flags
 *
 * @param <T> the flag enum
 * @param <U> the instance
 */
public class ConditionalPacketWriter<T extends Enum<T>, U> {
    protected static final long ALL_FLAGS = Long.MAX_VALUE;
    private final Class<T> flagClass;
    protected final Section<U>[] sections;
    protected final Section<U> defaultSection;

    protected ConditionalPacketWriter(Class<T> flagClass, Section<U> defaultSection, Section<U>[] sections) {
        assert sections.length <= 64;
        assert sections.length == flagClass.getEnumConstants().length;
        this.flagClass = flagClass;
        this.defaultSection = defaultSection;
        this.sections = sections;
    }

    protected void write(long flags, PacketByteBuf buf, U val) {
        if (this.defaultSection != null)
            this.defaultSection.writer.accept(buf, val);
        buf.writeVarLong(flags);
        for (int i = 0; i < this.sections.length; i++) {
            if ((flags & (1L << i)) != 0) {
                this.sections[i].writer.accept(buf, val);
            }
        }
    }

    protected void read(PacketByteBuf buf, U val) {
        if (this.defaultSection != null)
            this.defaultSection.reader.accept(buf, val);
        long flags = buf.readVarLong();
        for (int i = 0; i < this.sections.length; i++) {
            if ((flags & (1L << i)) != 0) {
                try {
                    this.sections[i].reader.accept(buf, val);
                } catch (Throwable throwable) {
                    throw new RuntimeException(
                      "Exception occurred whilst reading flag " + this.flagClass.getEnumConstants()[i].name(),
                      throwable
                    );
                }
            }
        }
    }

    /**
     * Creates a new state
     *
     * @param behaviour the write behaviour
     * @return the new state
     */
    public State createState(WriteBehaviour behaviour) {
        return new State(behaviour);
    }

    /**
     * Creates a new state, and marks every flag for writing
     *
     * @param behaviour the write behaviour
     * @return the new state
     */
    public State createFullState(WriteBehaviour behaviour) {
        State state = this.createState(behaviour);
        state.updateAll();
        return state;
    }

    /**
     * Creates a builder
     *
     * @param flagType an enum class to use as the flag type
     * @param value    the value class
     * @param <T>      the enum type
     * @param <U>      the value type
     * @return the new builder
     */
    @SuppressWarnings("unused")
    public static <T extends Enum<T>, U> Builder<T, U> builder(Class<T> flagType, Class<U> value) {
        return new Builder<>(flagType);
    }

    /**
     * Class that tracks marked flags for a {@link ConditionalPacketWriter}
     */
    public class State {
        private final WriteBehaviour behaviour;
        private long currentFlags = 0;

        protected State(WriteBehaviour behaviour) {
            this.behaviour = behaviour;
        }

        /**
         * Marks the given flag as needing to be written
         *
         * @param flag the flag to mark
         */
        public void update(T flag) {
            this.currentFlags |= (1L << flag.ordinal());
        }

        /**
         * Checks if any flag is marked for writing
         *
         * @return if any flag as been marked for writing on this state
         */
        public boolean isSet() {
            return this.currentFlags != 0;
        }

        /**
         * Writes to the packet using the previously marked flags
         *
         * @param buf the buffer to write to
         * @param val the instance to write
         */
        public void write(PacketByteBuf buf, U val) {
            if (this.behaviour == WriteBehaviour.ALL_ON_EMPTY && this.currentFlags == 0)
                this.currentFlags = ALL_FLAGS;
            ConditionalPacketWriter.this.write(this.currentFlags, buf, val);
            this.currentFlags = 0;
        }

        /**
         * Reads from the packet, only processing sections based off the received flags
         *
         * @param buf the buffer to read from
         * @param val the instance to read to
         */
        public void read(PacketByteBuf buf, U val) {
            ConditionalPacketWriter.this.read(buf, val);
        }

        /**
         * Marks all flags for writing
         */
        public void updateAll() {
            this.currentFlags = ALL_FLAGS;
        }
    }

    /**
     * How a State should act when no flags have been marked
     */
    public enum WriteBehaviour {
        /**
         * Writes all sections when no flags have been marked
         */
        ALL_ON_EMPTY,
        /**
         * Writes no sections when no flags have been marked
         */
        NONE_ON_EMPTY
    }

    protected static class Section<U> {
        protected BiConsumer<PacketByteBuf, U> writer;
        protected BiConsumer<PacketByteBuf, U> reader;

        public Section(BiConsumer<PacketByteBuf, U> writer, BiConsumer<PacketByteBuf, U> reader) {
            this.writer = writer;
            this.reader = reader;
        }
    }

    public static class Builder<T extends Enum<T>, U> {
        private final Class<T> flagClass;
        private Section<U> defaultSection;
        private final EnumMap<T, Section<U>> sections;

        protected Builder(Class<T> type) {
            this.flagClass = type;
            this.sections = new EnumMap<>(type);
        }

        /**
         * Defines a section that is always written/read to/from a packet
         *
         * @param writer the writer
         * @param reader the reader
         * @return this builder, for chaining
         */
        public Builder<T, U> defaultSection(BiConsumer<PacketByteBuf, U> writer, BiConsumer<PacketByteBuf, U> reader) {
            this.defaultSection = new Section<>(writer, reader);
            return this;
        }

        /**
         * Defines a section that is conditionally written/read to/from a packet
         *
         * @param writer the writer
         * @param reader the reader
         * @return this builder, for chaining
         */
        public Builder<T, U> section(T flag, BiConsumer<PacketByteBuf, U> writer, BiConsumer<PacketByteBuf, U> reader) {
            this.sections.put(flag, new Section<>(writer, reader));
            return this;
        }

        /**
         * Builds the conditional packet writer
         *
         * @return the final conditional packet writer
         */
        @SuppressWarnings("unchecked")
        public ConditionalPacketWriter<T, U> build() {
            return new ConditionalPacketWriter<>(
              this.flagClass,
              this.defaultSection,
              this.sections.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().ordinal()))
                .map(Map.Entry::getValue)
                .toArray(Section[]::new)
            );
        }
    }
}
