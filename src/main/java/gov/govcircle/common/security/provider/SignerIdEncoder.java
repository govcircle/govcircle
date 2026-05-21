package gov.govcircle.common.security.provider;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.transaction.spec.cert.StakePoolId;
import java.util.Arrays;

/**
 * Utilities to convert addresses / ids to the exact raw byte arrays
 * expected by SignerIdParser.parseSignerId(...)
 *
 * Notes:
 * - For CIP-129 gov ids (drep / cc_cold / cc_hot) we produce 29 bytes:
 *     [ headerByte ] + [ 28-byte keyHash ]
 *   The parser checks header nibble bits to distinguish types.
 *
 * - For legacy DRep (CIP-105) and for pool ids the wire form is the raw 28-byte
 *   payload (no header). This is inherently ambiguous (pool vs legacy drep).
 *
 * - Preferred input form is bech32 strings when available (e.g. "addr...", "stake...", "pool...", "drep...", "cc_cold...", "cc_hot...").
 *   We decode them using the library's Bech32.decode(...) which returns the original raw bytes (including header if encoded that way).
 *
 * - If you only have a 28-byte hex key-hash, pass it as hex or byte[] and use the helpers below.
 */
public final class SignerIdEncoder {

    private SignerIdEncoder() {}

    // Key-type header constants — use the same bit positions your parser used.
    // These correspond to the nibble positions checked earlier (0b1110_0000 etc).
    public static final byte DREP_HEADER = (byte) 0x20;      // 0b0010_0000
    public static final byte CC_COLD_HEADER = (byte) 0x10;   // 0b0001_0000
    public static final byte CC_HOT_HEADER = (byte) 0x00;    // 0b0000_0000

    private static final int KEYHASH_LEN = 28;
    private static final int CIP129_LEN = 29;

    // -------------------------
    // Generic utilities
    // -------------------------

    /** Hex (no 0x) -> bytes. Throws IllegalArgumentException on length mismatch. */
    public static byte[] hexToBytes(String hex) {
        if (hex == null) throw new IllegalArgumentException("hex == null");
        String h = hex.startsWith("0x") ? hex.substring(2) : hex;
        if ((h.length() & 1) != 0) throw new IllegalArgumentException("hex length must be even");
        byte[] out = new byte[h.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(h.substring(2 * i, 2 * i + 2), 16);
        }
        return out;
    }

    private static void assertKeyHashLen(byte[] keyHash) {
        if (keyHash == null || keyHash.length != KEYHASH_LEN) {
            throw new IllegalArgumentException("keyHash must be " + KEYHASH_LEN + " bytes");
        }
    }

    private static byte[] buildCip129Bytes(byte headerByte, byte[] keyHash) {
        assertKeyHashLen(keyHash);
        byte[] out = new byte[CIP129_LEN];
        out[0] = headerByte;
        System.arraycopy(keyHash, 0, out, 1, KEYHASH_LEN);
        return out;
    }

    // -------------------------
    // Address & bech32 based converters
    // -------------------------

    /**
     * Payment or stake address (bech32). Returns the exact raw address bytes that
     * Address(byte[]) constructor expects. Example inputs: "addr1...", "stake1..."
     */
    public static byte[] addressBech32ToBytes(String bech32Address) {
        if (bech32Address == null) throw new IllegalArgumentException("bech32Address == null");
        // Bech32.decode should return the original raw bytes. Adjust if your lib has a different method.
        return Bech32.decode(bech32Address).data;
    }

    /**
     * If you already have an Address object, use this. Many Address implementations
     * expose either toBytes() or getBytes(); try to call toBytes() first.
     *
     * If your Address class uses a different method name, change accordingly.
     */
    public static byte[] addressToBytes(Address address) {
        if (address == null) throw new IllegalArgumentException("address == null");
        // Most cardano-client libs expose a toBytes() or toEntityBytes(); adjust if needed.
        // Attempt common method names; compiler error -> change to the correct one from the library.
        try {
            // preferred: Address::toBytes()
            return address.getBytes();
        } catch (NoSuchMethodError | AbstractMethodError e) {
            // fallback: try getBytes()
            try {
                return address.getBytes();
            } catch (NoSuchMethodError nsme) {
                throw new RuntimeException("Address has no toBytes()/getBytes() method in your library version. Please adapt this call.");
            }
        }
    }

    // -------------------------
    // Stake pool / legacy drep converters
    // -------------------------

    /**
     * Pool id as bech32 (e.g. "pool1...") -> raw 28 bytes (pool key-hash).
     * The returned array is exactly 28 bytes and will be recognized by your parser
     * as a potential pool id (but note: 28-byte payloads are ambiguous with legacy DRep).
     */
    public static byte[] poolBech32ToBytes(String poolBech32) {
        if (poolBech32 == null) throw new IllegalArgumentException("poolBech32 == null");
        return Bech32.decode(poolBech32).data; // expected length 28
    }

    /** If you already have the 28-byte pool keyhash as hex or bytes, return it directly. */
    public static byte[] poolKeyHashToBytes(byte[] poolKeyHash) {
        assertKeyHashLen(poolKeyHash);
        return Arrays.copyOf(poolKeyHash, KEYHASH_LEN);
    }

    /** If you have pool key-hash hex string: */
    public static byte[] poolKeyHashHexToBytes(String hex) {
        byte[] b = hexToBytes(hex);
        return poolKeyHashToBytes(b);
    }

    /**
     * Legacy DRep (CIP-105) raw 28 byte payload. If you only have the key-hash give that here.
     * Parser will treat 28 bytes as possible legacy drep when attempting legacy conversion.
     */
    public static byte[] legacyDrepFromKeyHash(byte[] keyHash) {
        assertKeyHashLen(keyHash);
        return Arrays.copyOf(keyHash, KEYHASH_LEN);
    }

    public static byte[] legacyDrepFromHex(String keyHashHex) {
        return legacyDrepFromKeyHash(hexToBytes(keyHashHex));
    }

    /**
     * If you have a legacy-drep bech32 string (rare), decode it: may return 28 bytes.
     */
    public static byte[] legacyDrepBech32ToBytes(String drepBech32) {
        if (drepBech32 == null) throw new IllegalArgumentException("drepBech32 == null");
        return Bech32.decode(drepBech32).data; // often 28 bytes (no header)
    }

    // -------------------------
    // CIP-129 Gov ids (DRep / CC) -> 29 bytes
    // -------------------------

    /**
     * Given a 28-byte keyHash and a gov header constant, construct the CIP-129 bytes
     * that SignerIdParser will detect as GovId/CIP-129.
     *
     * Example usage:
     *   byte[] drepBytes = cip129FromKeyHash(DREP_HEADER, keyHash);
     *
     * Then Bech32.encode(drepBytes,"drep") will produce a CIP-129 drep bech32 string.
     */
    public static byte[] cip129FromKeyHash(byte headerConstant, byte[] keyHash) {
        return buildCip129Bytes(headerConstant, keyHash);
    }

    public static byte[] cip129FromKeyHashHex(byte headerConstant, String keyHashHex) {
        return cip129FromKeyHash(headerConstant, hexToBytes(keyHashHex));
    }

    /**
     * If you already have a CIP-129 bech32 string (e.g. "drep1..." produced by Bech32.encode(headerBytes,"drep")),
     * decode it to the original 29 bytes.
     */
    public static byte[] cip129Bech32ToBytes(String bech32) {
        if (bech32 == null) throw new IllegalArgumentException("bech32 == null");
        return Bech32.decode(bech32).data; // expected 29 bytes for CIP-129 encoded items
    }

    // Convenient named constructors

    public static byte[] drepCip129FromKeyHash(byte[] drepKeyHash) {
        return cip129FromKeyHash(DREP_HEADER, drepKeyHash);
    }
    public static byte[] ccColdCip129FromKeyHash(byte[] ccColdKeyHash) {
        return cip129FromKeyHash(CC_COLD_HEADER, ccColdKeyHash);
    }
    public static byte[] ccHotCip129FromKeyHash(byte[] ccHotKeyHash) {
        return cip129FromKeyHash(CC_HOT_HEADER, ccHotKeyHash);
    }

    // -------------------------
    // Example small test-driver (call from your unit tests)
    // -------------------------
    public static void main(String[] args) throws Exception {
        // Example: create CIP-129 DRep bytes from hex keyHash and validate parse
        String exampleKeyHashHex = "0123456789abcdef0123456789abcdef0123456789abcdef01234567".substring(0, KEYHASH_LEN*2);
        byte[] keyHash = hexToBytes(exampleKeyHashHex);

        byte[] cip129 = drepCip129FromKeyHash(keyHash); // 29 bytes
        System.out.println("cip129.length = " + cip129.length);

        byte[] legacy = legacyDrepFromKeyHash(keyHash); // 28 bytes
        System.out.println("legacy.length = " + legacy.length);

        // If you have a pool bech32 string, decode:
        // byte[] poolBytes = poolBech32ToBytes("pool1...");
        // System.out.println("pool length = " + poolBytes.length);
    }
}
