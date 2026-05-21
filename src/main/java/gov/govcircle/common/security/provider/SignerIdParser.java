package gov.govcircle.common.security.provider;

// imports you'll need (adjust package as needed)
import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.transaction.spec.governance.DRep;
import com.bloxbean.cardano.client.util.HexUtil;
import java.util.Optional;

/**
 * Parse a COSE "address" header bytes which might be a normal address
 * or a governance DRep id (CIP-129/CIP-95 style).
 *
 * Returns either Address (in Optional.left-like object) OR the DRep (as bech32 String or DRep).
 * For simplicity below I return an object describing what we found.
 */
public class SignerIdParser {

    public static class SignerId {
        public final Address address;     // non-null if this is an address
        public final DRep drep;          // non-null if this is a drep (DRep object from repo)
        public final String drepBech32;  // alternate: bech32 string for drep

        private SignerId(Address a, DRep d, String b) {
            this.address = a;
            this.drep = d;
            this.drepBech32 = b;
        }

        public static SignerId ofAddress(Address a) { return new SignerId(a, null, null); }
        public static SignerId ofDrep(DRep d, String bech32) { return new SignerId(null, d, bech32); }
    }

    private static final byte DREP_KEY_TYPE_MASK = (byte)0b1110_0000;
    private static final byte DREP_KEY_TYPE = (byte)0b0010_0000;

    /**
     * Try to parse header bytes coming from DataSignature.address()
     * - If bytes represent a normal Shelley/Byron address, returns Address.
     * - If bytes represent gov DRep id bytes, returns DRep (via GovId.toDrep()).
     */
    public static SignerId parseSignerId(byte[] headerBytes) {
        if (headerBytes == null || headerBytes.length == 0)
            return null;

        // 1) Quick detect for GovId/DRep bytes
        // GovId produced idBytes: 1 header + 28 keyHash => total 29 bytes
        if (headerBytes.length == 29) {
            byte header = headerBytes[0];
            if ((header & DREP_KEY_TYPE_MASK) == DREP_KEY_TYPE) {
                // looks like a DRep id bytes (CIP-129 style)
                String drepBech32 = Bech32.encode(headerBytes, "drep");
                // convert to DRep object using existing helper
                DRep drep = GovId.toDrep(drepBech32);
                return SignerId.ofDrep(drep, drepBech32);
            }
        }

        // 2) fallback: try constructing Address (this will validate header/network)
        try {
            Address addr = new Address(headerBytes);
            return SignerId.ofAddress(addr);
        } catch (Exception e) {
            // 3) maybe it's a legacy DRep id (or other unknown type) — try a couple more checks:
            // - Legacy CIP105 DRepId (LegacyDRepId) used bech32 with raw 28 bytes payload.
            if (headerBytes.length == 28) {
                // try to treat as legacy drep payload: encode as bech32 with "drep" prefix
                String bech = Bech32.encode(headerBytes, "drep");
                try {
                    DRep drep = com.bloxbean.cardano.client.governance.LegacyDRepId.toDrep(bech, /* choose DRepType if needed */ com.bloxbean.cardano.client.transaction.spec.governance.DRepType.ADDR_KEYHASH);
                    return SignerId.ofDrep(drep, bech);
                } catch (Exception ex) {
                    // not legacy either
                }
            }
            // give up — return null or rethrow as appropriate for your flow
            return null;
        }
    }
}
