//package gov.govcircle.common.security.provider;
//
//package com.yourorg.cardano.util; // change to appropriate package
//
//import com.bloxbean.cardano.client.address.Address;
//import com.bloxbean.cardano.client.address.AddressRuntimeException;
//import com.bloxbean.cardano.client.crypto.Bech32;
//import com.bloxbean.cardano.client.governance.GovId;
//// NOTE: the following imports assume these types exist in your repo.
//// If different, change to correct classes / factory methods.
//import com.bloxbean.cardano.client.transaction.spec.governance.DRep;
//import com.bloxbean.cardano.client.transaction.spec.governance.Spo; // TODO: adjust name
//import com.bloxbean.cardano.client.transaction.spec.governance.Cc;  // TODO: adjust name
//
///**
// * Parse a COSE "address" header bytes which might be:
// *  - a normal payment/stake address,
// *  - a DRep id (governance),
// *  - an SPO id (stake pool operator),
// *  - a CC id (committee / other governance id)
// *
// * This parser:
// *  - does quick detection using header byte masks and length checks,
// *  - produces a typed result indicating what was found.
// *
// * IMPORTANT: verify header mask constants below against your repository's GovId class.
// */
//public class SignerIdParser {
//
//    public static class SignerId {
//        public final Address address;     // non-null if this is an address
//        public final DRep drep;           // non-null if this is a DRep
//        public final Spo spo;             // non-null if this is a SPO id
//        public final Cc cc;               // non-null if this is a CC id
//        public final String bech32;       // bech32 representation (if any)
//
//        private SignerId(Address a, DRep d, Spo s, Cc c, String b) {
//            this.address = a;
//            this.drep = d;
//            this.spo = s;
//            this.cc = c;
//            this.bech32 = b;
//        }
//
//        public static SignerId ofAddress(Address a) { return new SignerId(a, null, null, null, null); }
//        public static SignerId ofDrep(DRep d, String bech32) { return new SignerId(null, d, null, null, bech32); }
//        public static SignerId ofSpo(Spo s, String bech32) { return new SignerId(null, null, s, null, bech32); }
//        public static SignerId ofCc(Cc c, String bech32) { return new SignerId(null, null, null, c, bech32); }
//    }
//
//    // Mask to isolate the key-type bits from the header byte (top 3 bits used by GovId impls)
//    private static final byte KEY_TYPE_MASK = (byte) 0b1110_0000;
//
//    // TODO: Replace these with the exact constants from your GovId/Governance classes.
//    // Example placeholders:
//    private static final byte DREP_KEY_TYPE = (byte) 0b0010_0000; // example: 0x20
//    private static final byte SPO_KEY_TYPE  = (byte) 0b0100_0000; // example placeholder
//    private static final byte CC_KEY_TYPE   = (byte) 0b0110_0000; // example placeholder
//
//    // Common length for gov ids in many implementations: header + 28 bytes of keyHash = 29
//    private static final int GOV_ID_TOTAL_LEN = 29;
//    // Some legacy or other forms may be 28 bytes (payload only) — keep legacy handling
//    private static final int LEGACY_PAYLOAD_LEN = 28;
//
//    /**
//     * Parse headerBytes retrieved from DataSignature (COSE "address" header).
//     * Returns SignerId describing the detected signer, or null if unknown / cannot parse.
//     */
//    public static SignerId parseSignerId(byte[] headerBytes) {
//        if (headerBytes == null || headerBytes.length == 0) return null;
//
//        // Quick detection: governance IDs which are (header + 28) bytes long (typical)
//        if (headerBytes.length == GOV_ID_TOTAL_LEN) {
//            byte header = headerBytes[0];
//            byte keyType = (byte) (header & KEY_TYPE_MASK);
//
//            // DRep
//            if (keyType == DREP_KEY_TYPE) {
//                String drepBech32 = Bech32.encode(headerBytes, "drep");
//                // Use existing helper to build DRep object. Adjust method name as in your repo.
//                DRep drep = GovId.toDrep(drepBech32); // <-- verify method name in your code
//                return SignerId.ofDrep(drep, drepBech32);
//            }
//
//            // SPO (Stake Pool Operator)
//            if (keyType == SPO_KEY_TYPE) {
//                // Common bech32 prefix for pools usually "pool" in Cardano ecosystem,
//                // but verify exact prefix used by GovId in your repo.
//                String poolBech32 = Bech32.encode(headerBytes, "pool");
//                // Create SPO object via repo helper. Replace GovId.toSpo(...) with real method.
//                Spo spo = GovId.toSpo(poolBech32); // <-- verify method name / class
//                return SignerId.ofSpo(spo, poolBech32);
//            }
//
//            // CC (Committee / other governance id)
//            if (keyType == CC_KEY_TYPE) {
//                // Use a sensible bech32 prefix (change as appropriate)
//                String ccBech32 = Bech32.encode(headerBytes, "cc"); // adjust prefix
//                Cc cc = GovId.toCc(ccBech32); // <-- verify method name / class
//                return SignerId.ofCc(cc, ccBech32);
//            }
//
//            // If header length matches but keyType didn't match known constants,
//            // return bech32 string for manual inspection (optional)
//            // String genericBech = Bech32.encode(headerBytes, "gov"); // optional
//            // return a SignerId with null typed fields and bech32 if you want.
//        }
//
//        // Legacy / alternative formats:
//        // e.g., some implementations used raw 28-byte keyHash payloads (no leading header).
//        if (headerBytes.length == LEGACY_PAYLOAD_LEN) {
//            // Try DRep legacy decoder (some projects have LegacyDRepId)
//            try {
//                String legacyDrepBech = Bech32.encode(headerBytes, "drep");
//                // If your repo has LegacyDRepId / helper:
//                DRep drep = com.bloxbean.cardano.client.governance.LegacyDRepId.toDrep(legacyDrepBech);
//                return SignerId.ofDrep(drep, legacyDrepBech);
//            } catch (Exception ex) {
//                // ignore and continue
//            }
//            // You could also try legacy pool id handling here if applicable.
//        }
//
//        // Fallback: try as a normal Address — Address constructor validates network bits etc.
//        try {
//            Address addr = new Address(headerBytes);
//            return SignerId.ofAddress(addr);
//        } catch (Exception e) {
//            // Not a normal address, not a recognized gov id — return null (or throw)
//            return null;
//        }
//    }
//}
