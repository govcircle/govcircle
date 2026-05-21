package gov.govcircle.common.security.provider;

// ExampleSignerForRoleKeys.java

import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip8.COSEKey;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import com.bloxbean.cardano.client.cip.cip8.Headers;
import com.bloxbean.cardano.client.cip.cip8.HeaderMap;
import com.bloxbean.cardano.client.cip.cip8.ProtectedHeaderMap;
import com.bloxbean.cardano.client.cip.cip8.builder.COSESign1Builder;
import com.bloxbean.cardano.client.cip.cip8.SigStructure;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.config.Configuration;
import com.bloxbean.cardano.client.util.HexUtil;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rest.koios.client.utils.Bech32Util;
import rest.koios.client.utils.Tuple;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class RoleSigner {

    // Parse cardano-cli key file JSON and extract the CBOR byte string inside cborHex robustly
    public static byte[] extractBytesFromCborHex(String cborHex) {
        // cborHex normally starts with hex for CBOR byte string: 58 <len> <bytes>
        // Common pattern in cardano-cli files: "cborHex": "5820<32bytehex>" -> 0x58 0x20 then 32 bytes
        byte[] raw = HexUtil.decodeHexString(cborHex);
        if (raw.length >= 2 && (raw[0] & 0xFF) == 0x58) { // major type 2 byte string with "one byte length"
            int len = raw[1] & 0xFF;
            int offset = 2;
            if (raw.length >= offset + len) {
                byte[] out = new byte[len];
                System.arraycopy(raw, offset, out, 0, len);
                return out;
            } else {
                throw new IllegalArgumentException("CBOR hex shorter than expected");
            }
        } else {
            // fallback: if cborHex directly contains the raw bytes (rare), try to locate 32 or 64 byte seq
            if (raw.length == 32 || raw.length == 64) return raw;
            // last attempt: if file contains CBOR wrapper, find the first 32/64 contiguous bytes
            for (int i = 0; i < raw.length; i++) {
                int remain = raw.length - i;
                if (remain >= 32) {
                    byte[] cand = new byte[32];
                    System.arraycopy(raw, i, cand, 0, 32);
                    return cand;
                }
            }
            throw new IllegalArgumentException("Could not extract key bytes from cborHex");
        }
    }

    public static byte[] readCborHexFromJsonFile(File keyFile) throws Exception {
        String s = new String(Files.readAllBytes(keyFile.toPath()));
        ObjectMapper om = new ObjectMapper();
        JsonNode n = om.readTree(s);
        JsonNode cbor = n.get("cborHex");
        if (cbor == null)
            throw new IllegalArgumentException("missing cborHex in " + keyFile);
        return extractBytesFromCborHex(cbor.asText());
    }

    /**
     * Sign payload with given role ID bytes (roleIdBytes). roleIdBytes can be:
     * - pool id bytes (hex) for SPO (obtain via cardano-cli stake-pool id --cold-verification-key-file)
     * - an arbitrary id for DRep/CC (provided by you)
     * <p>
     * privateKey: either 32-bytes (normal ed25519 sk) or 64-bytes (extended/expanded sk)
     * publicKey: 32 bytes
     */
    public static DataSignature signForRole(
            byte[] payload,
            byte[] privateKey,
            byte[] publicKey,
            byte[] roleIdBytes,
            boolean hashPayload
    ) {
        // Build protected header map
        HeaderMap protectedHeaderMap = new HeaderMap()
                .algorithmId(-8L)
                .keyId(roleIdBytes)
                .addOtherHeader(
                        "address",
                        new ByteString(roleIdBytes)
                ); // re-uses "address" header slot

        Headers headers = new Headers()
                ._protected(new ProtectedHeaderMap(protectedHeaderMap))
                .unprotected(new HeaderMap());

        // Build COSESign1 builder (same as CIP30)
        COSESign1Builder builder = new COSESign1Builder(
                headers,
                payload,
                false
        )
                .hashed(hashPayload);
        SigStructure sigStructure = builder.makeDataToSign();

        // Sign using configured SigningProvider (same logic CIP30 uses)
        byte[] signature;
        if (privateKey.length >= 64) {
            signature = Configuration.INSTANCE
                    .getSigningProvider()
                    .signExtended(
                            sigStructure.serializeAsBytes(),
                            privateKey
                    );

        } else {
            signature = Configuration.INSTANCE
                    .getSigningProvider()
                    .sign(
                            sigStructure.serializeAsBytes(),
                            privateKey
                    );

        }

        // Build COSESign1 and COSEKey objects
        COSESign1 coseSign1 = builder.build(signature);

        COSEKey coseKey = new COSEKey()
                .keyType(1L)
                .keyId(roleIdBytes)
                .algorithmId(-8L)
                .addOtherHeader(
                        -1L,
                        new UnsignedInteger(6L)
                )
                .addOtherHeader(
                        -2L,
                        new ByteString(publicKey)
                );
        // Compose DataSignature (same structure used by CIP30DataSigner)
        return new DataSignature()
                .signature(HexUtil.encodeHexString(coseSign1.serializeAsBytes()))
                .key(HexUtil.encodeHexString(coseKey.serializeAsBytes()));

    }

    public static Tuple<String, byte[]> bech32Decode(String bech32EncodedString) {
        char SEPARATOR = '1';
        String B_32_CHARS = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
        bech32EncodedString = bech32EncodedString.toLowerCase();

        int separatorIndex = bech32EncodedString.lastIndexOf(SEPARATOR);
        String hrp = bech32EncodedString.substring(0, separatorIndex);
        String data = bech32EncodedString.substring(separatorIndex + 1);

        byte[] b32Arr = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            b32Arr[i] = (byte) B_32_CHARS.indexOf(data.charAt(i));

        }
        return new Tuple<>(hrp, b32Arr);

    }

    // small usage example:
    public static void main(String[] args) throws Exception {
        // args: <payload-file> <signing.skey.json> <verification.vkey.json> <roleIdHex>
        byte[] payload = Files.readAllBytes(new File(args[0]).toPath());
        byte[] sk = readCborHexFromJsonFile(new File(args[1])); // signing key (skey)
        byte[] pk = readCborHexFromJsonFile(new File(args[2])); // verification key (vkey)
        byte[] roleId = HexUtil.decodeHexString(args[3]); // your single ID (poolId/DRepId/CCId) as hex


        DataSignature ds = signForRole(payload, sk, pk, roleId, true); // true if you want hashed payload behavior
        System.out.println(ds.toString()); // JSON containing signature and key

    }

}
