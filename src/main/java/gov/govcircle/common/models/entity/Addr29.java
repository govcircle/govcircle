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
public class Addr29 {

    @Lob
    @Column(
            name = "value",
            columnDefinition = "addr29type",
            nullable = false
    )
    private byte[] value;

    public Addr29(byte[] value) {
        if (value == null || value.length != 29) {
            throw new IllegalArgumentException("Addr29 must be exactly 29 bytes");

        }
        this.value = value;

    }

    public byte[] getValue() {
        return value;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Addr29 other)) return false;
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
        return "Addr29[" + value.length + " bytes]";

    }

}
