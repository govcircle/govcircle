package gov.govcircle.common.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
public class Hash28 {

    @Lob
    @Column(
            name = "value",
            columnDefinition = "hash28type",
            nullable = false
    )
    private byte[] value;

    public Hash28(byte[] value) {
        if (value == null || value.length != 28) {
            throw new IllegalArgumentException("Hash28 must be exactly 28 bytes");

        }
        this.value = value;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hash28 other)) return false;
        return Arrays.equals(
                value,
                other.value
        );

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);

    }

    @Override
    public String toString() {
        return "Hash28[" + value.length + " bytes]";

    }

}
