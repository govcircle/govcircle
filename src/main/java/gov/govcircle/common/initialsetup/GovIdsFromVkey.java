package gov.govcircle.common.initialsetup;

// GovIdsFromVkey.java
import java.nio.file.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.LegacyDRepId;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.util.HexUtil;

public class GovIdsFromVkey {

    // Read VerificationKey JSON (cardano-cli format with cborHex)
    public static VerificationKey readVkey(Path vkeyPath) throws Exception {
        String json = Files.readString(vkeyPath);
        ObjectMapper om = new ObjectMapper();
        return om.readValue(json, VerificationKey.class);
    }

    // Legacy CIP-105 DRep (bech32 "drep")
    public static String drepCip105(VerificationKey vkey) throws Exception {
        // Legacy helper (exists in the lib)
        return LegacyDRepId.fromVerificationKey(vkey);
        // or manual:
        // byte[] keyBytes = vkey.getBytes();
        // byte[] keyHash = Blake2bUtil.blake2bHash224(keyBytes);
        // return Bech32.encode(keyHash, "drep");
    }

    // CIP-129 DRep (new header format)
    public static String drepCip129(VerificationKey vkey) throws Exception {
        byte[] keyBytes = vkey.getBytes();
        byte[] keyHash = Blake2bUtil.blake2bHash224(keyBytes);
        return GovId.drepFromKeyHash(keyHash);
    }

    // CC cold/hot (CIP-129)
    public static String ccColdCip129(VerificationKey vkey) throws Exception {
        byte[] keyHash = Blake2bUtil.blake2bHash224(vkey.getBytes());
        return GovId.ccColdFromKeyHash(keyHash);
    }
    public static String ccHotCip129(VerificationKey vkey) throws Exception {
        byte[] keyHash = Blake2bUtil.blake2bHash224(vkey.getBytes());
        return GovId.ccHotFromKeyHash(keyHash);
    }

    // Stake pool id (bech32 "pool" or hex)
    public static String poolIdFromColdVkey(VerificationKey coldVkey) throws Exception {
        byte[] keyHash = Blake2bUtil.blake2bHash224(coldVkey.getBytes());
        // hex:
        String hex = HexUtil.encodeHexString(keyHash);
        // bech32:
        String poolBech = Bech32.encode(keyHash, "pool");
        return "hex: " + hex + "  bech32: " + poolBech;
    }

    // Example main
    public static void main(String[] args) throws Exception {
        // args: <drep.vkey> <cc-cold.vkey> <pool-cold.vkey>
        VerificationKey drepVkey = readVkey(Paths.get(args[0]));
        VerificationKey ccCold = readVkey(Paths.get(args[1]));
        VerificationKey poolCold = readVkey(Paths.get(args[2]));

        System.out.println("CIP-105 drep: " + drepCip105(drepVkey));
        System.out.println("CIP-129 drep: " + drepCip129(drepVkey));
        System.out.println("CIP-129 cc_cold: " + ccColdCip129(ccCold));
        System.out.println("pool id: " + poolIdFromColdVkey(poolCold));
    }
}
