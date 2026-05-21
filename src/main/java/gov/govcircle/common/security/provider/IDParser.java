package gov.govcircle.common.security.provider;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.address.Credential;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.governance.LegacyDRepId;
import com.bloxbean.cardano.client.transaction.spec.cert.StakePoolId;
import com.bloxbean.cardano.client.transaction.spec.governance.DRep;
import com.bloxbean.cardano.client.transaction.spec.governance.DRepType;
import com.bloxbean.cardano.client.util.HexUtil;

import java.util.Objects;

public class IDParser {

    public static class SignerId {
        public final Address address;          // non-null if parsed as an address
        public final DRep drep;                // non-null if parsed as a CIP-129 DRep
        public final String drepBech32;        // bech32 for the CIP-129 DRep (if any)

        public final DRep legacyDrepCandidate; // non-null if legacy (CIP-105 style) DRep possible
        public final String legacyDrepBech32;  // bech32 for legacy drep candidate (if any)

        public final StakePoolId pool;         // non-null if interpreted as a pool id (28 bytes)
        public final String poolBech32;        // bech32 pool id (if any)

        // credential constructed from cc ids (cc_cold / cc_hot) — useful for using as an address credential
        public final Credential ccCredential;
        public final String ccBech32;

        // If more than one candidate is set (e.g. pool + legacyDrepCandidate) client should treat as ambiguous.
        public SignerId(
                Address address,
                DRep dRep,
                String dRepBech32,
                DRep legacyDRepCandidate,
                String legacyDRepBech32,
                StakePoolId pool,
                String poolBech32,
                Credential ccCredential,
                String ccBech32
        ) {
            this.address = address;
            this.drep = dRep;
            this.drepBech32 = dRepBech32;
            this.legacyDrepCandidate = legacyDRepCandidate;
            this.legacyDrepBech32 = legacyDRepBech32;
            this.pool = pool;
            this.poolBech32 = poolBech32;
            this.ccCredential = ccCredential;
            this.ccBech32 = ccBech32;
        }

        public static SignerId ofAddress(Address address) {
            return new SignerId(
                    address,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

        }

        public static SignerId ofDrep(
                DRep d,
                String drepBech32
        ) {
            return new SignerId(
                    null,
                    d,
                    drepBech32,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

        }

        public static SignerId ofPool(
                StakePoolId pool,
                String poolBech32
        ) {
            return new SignerId(
                    null,
                    null,
                    null,
                    null,
                    null,
                    pool,
                    poolBech32,
                    null,
                    null
            );

        }

        public static SignerId ofCc(
                Credential cc,
                String ccBech32
        ) {
            return new SignerId(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    cc,
                    ccBech32
            );

        }

        public static SignerId ambiguous(
                StakePoolId pool,
                String poolBech32,
                DRep legacyDrep,
                String legacyBech32
        ) {
            return new SignerId(
                    null,
                    null,
                    null,
                    legacyDrep,
                    legacyBech32,
                    pool,
                    poolBech32,
                    null,
                    null
            );

        }
    }

    // masks and key-type constants (matching GovId/CIP-129 bit layout)
    private static final byte KEY_TYPE_MASK = (byte) 0b1111_0000;
    private static final byte DREP_KEY_TYPE = (byte) 0b0010_0000;
    private static final byte CC_COLD_KEY_TYPE = (byte) 0b0001_0000;
    private static final byte CC_HOT_KEY_TYPE = (byte) 0b0000_0000;

    /**
     * Extend existing parser:
     * - 29 bytes -> check header nibble: DRep / CC_COLD / CC_HOT
     * - fallback: try Address
     * - if still failing and length==28 -> produce pool candidate and legacy-drep candidate (ambiguous)
     */
    public static SignerId parseSignerId(byte[] headerBytes) {
        if (Objects.isNull(headerBytes) || headerBytes.length == 0) return null;

        // 1) 29-byte CIP-129 style identifiers (header + 28)
        if (headerBytes.length == 29) {
            byte header = headerBytes[0];
            byte keyType = (byte) (header & KEY_TYPE_MASK);

            if (keyType == DREP_KEY_TYPE) {
                String dRepBech32 = Bech32.encode(headerBytes, "drep");
                DRep drep = GovId.toDrep(dRepBech32); // uses CIP-129 path
                return SignerId.ofDrep(drep, dRepBech32);

            }
            if (keyType == CC_COLD_KEY_TYPE) {
                String ccColdBech32 = Bech32.encode(headerBytes, "cc_cold");
                Credential cred = GovId.ccColdToCredential(ccColdBech32);
                return SignerId.ofCc(cred, ccColdBech32);

            }

            if (keyType == CC_HOT_KEY_TYPE) {
                String ccHotBech32 = Bech32.encode(headerBytes, "cc_hot");
                Credential cred = GovId.ccHotToCredential(ccHotBech32);
                return SignerId.ofCc(cred, ccHotBech32);
            }

            // other gov key types could be added here
        }

        // 2) Try parse as regular Cardano Address
        try {
            Address addr = new Address(headerBytes);
            return SignerId.ofAddress(addr);

        } catch (Exception ignored) {
            // fallthrough to other heuristics

        }

        // 3) If 28 bytes: ambiguous zone (pool id vs legacy drep vs generic keyhash)
        if (headerBytes.length == 28) {
            // candidate #1: stake pool id
            try {
                StakePoolId pool = new StakePoolId(headerBytes);
                String bech32PoolId = pool.getBech32PoolId();

                // candidate #2: legacy DRep (CIP-105 style) — still possible
                String legacyDrepBech = Bech32.encode(
                        headerBytes,
                        "drep"
                );
                try {
                    // caller has to pick which semantics they want. We'll attempt to construct a DRep (addr-keyhash)
                    DRep legacyDrep = GovId.toDrep(legacyDrepBech);
                    // both pool and legacy drep succeeded — ambiguous
                    return SignerId.ambiguous(
                            pool,
                            bech32PoolId,
                            legacyDrep,
                            legacyDrepBech
                    );

                } catch (Exception exLegacy) {
                    // legacy DRep conversion failed -> return pool
                    return SignerId.ofPool(pool, bech32PoolId);

                }

            } catch (Exception ex) {
                // if we can't construct pool (unlikely for raw 28 bytes), try legacy drep
                try {
                    String legacyDrepBech = Bech32.encode(headerBytes, "drep");
                    DRep legacyDrep = LegacyDRepId.toDrep(legacyDrepBech, DRepType.ADDR_KEYHASH);
                    return SignerId.ofDrep(legacyDrep, legacyDrepBech);
                } catch (Exception e2) {
                    // give up
                }
            }
        }

        // 4) give up — unknown
        return null;
    }
}
