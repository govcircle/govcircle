package gov.govcircle.common.util;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.address.AddressProvider;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import com.bloxbean.cardano.client.config.Configuration;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.transaction.spec.cert.StakePoolId;
import gov.govcircle.common.security.model.dto.InMemoryActorAuthenticationIdentifierDTO;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import java.util.function.Function;

import static com.bloxbean.cardano.client.crypto.Blake2bUtil.blake2bHash224;

public class GovCircleSignatureUtils {

    public static final String ADDRESS_HEADER = "address";
    public static final Integer PUBLIC_KEY_HEADER = -2;

    public static boolean verifySignature(
            DataSignature dataSignature,
            boolean checkNonce,
            Function<String, InMemoryActorAuthenticationIdentifierDTO> applicationUserSupplier
    ) {
        COSESign1 frontEndSignature = dataSignature.coseSign1();
        String noncePayload = new String(
                frontEndSignature.payload()
        );

        byte[] cip8AddressBytes = getCip8AddressBytesFromDataSignature(dataSignature);
        CardanoActorType actorType = getDataSignatureActorType(dataSignature);
        byte[] publicKey = getCip8PublicKeyFromDataSignature(dataSignature);
        byte[] publicKeyHash = getCip8PublicKeyHashFromDataSignature(dataSignature);
        String keyIdentifier = getKeyIdentifierFromDataSignature(dataSignature);
        boolean dataVerified = checkNonce
                ? applicationUserSupplier
                .apply(keyIdentifier)
                .getPassword()
                .equals(noncePayload)
                : Boolean.TRUE;
        if (actorType.equals(CardanoActorType.DREP) && cip8AddressBytes.length != 29) {
            keyIdentifier = GovCircleGovernanceUtils.dRepCip129fromByte(cip8AddressBytes);

        }
        boolean addressVerified = switch (actorType) {
            case DREP -> {
                String cip129DRepIdFromPublicKeyHash = GovId.drepFromKeyHash(publicKeyHash);
                yield cip129DRepIdFromPublicKeyHash.equals(keyIdentifier);

            }
            case WALLET -> {
                Address address = new Address(cip8AddressBytes);
                yield AddressProvider.verifyAddress(
                        address,
                        publicKey
                );

            }
            case SPO -> {
                String poolIdFromPublicKeyHash = new StakePoolId(publicKeyHash).getBech32PoolId();
                yield poolIdFromPublicKeyHash.equals(keyIdentifier);

            }
            case CC -> {
                String cip129CCIdFromPublicKeyHash = GovId.ccColdFromKeyHash(publicKeyHash);
                yield cip129CCIdFromPublicKeyHash.equals(keyIdentifier);

            }

        };
        byte[] sigStructureBytes = frontEndSignature
                .signedData()
                .serializeAsBytes();
        byte[] signature = frontEndSignature.signature();
        boolean signatureVerified = Configuration.INSTANCE.getSigningProvider()
                .verify(
                        signature,
                        sigStructureBytes,
                        publicKey
                );
       return signatureVerified && addressVerified && dataVerified;

    }

    public static CardanoActorType getDataSignatureActorType(
            DataSignature dataSignature
    ) {
        return CardanoActorType.valueOf(
                getDataSignaturePayload(dataSignature)
                        .split(":")[0]
                        .trim()
        );

    }
    public static String getDataSignaturePayload(
            DataSignature dataSignature // Add custom wildcard <T> for custom conversion
    ) {
        return new String(
                dataSignature
                        .coseSign1()
                        .payload()
        );

    }
    public static byte[] getCip8AddressBytesFromDataSignature(
            DataSignature dataSignature
    ) {
        return dataSignature.coseSign1()
                .headers()
                ._protected()
                .getAsHeaderMap()
                .otherHeaderAsBytes(ADDRESS_HEADER);

    }
    public static byte[] getCip8PublicKeyHashFromDataSignature(
            DataSignature dataSignature
    ) {
        return blake2bHash224(getCip8PublicKeyFromDataSignature(dataSignature));

    }
    public static byte[] getCip8PublicKeyFromDataSignature(
            DataSignature dataSignature
    ) {
        return dataSignature
                .coseKey()
                .otherHeaderAsBytes(PUBLIC_KEY_HEADER);

    }
    public static String getKeyIdentifierFromDataSignature(
            DataSignature dataSignature
    ) {
        byte[] cip8AddressBytes = getCip8AddressBytesFromDataSignature(dataSignature);
        CardanoActorType actorType = getDataSignatureActorType(dataSignature);
        return getKeyIdentifierFromCip8AddressBytes(
                cip8AddressBytes,
                actorType
        );

    }

    public static String getKeyIdentifierFromCip8AddressBytes(
            byte[] cip8AddressBytes,
            CardanoActorType cardanoActorType
    ) {
        return switch (cardanoActorType) {
            case DREP -> GovCircleGovernanceUtils.dRepIdFromByte(cip8AddressBytes);
            case WALLET -> new Address(cip8AddressBytes).toBech32();
            case SPO -> new StakePoolId(cip8AddressBytes).getBech32PoolId();
            case CC -> Bech32.encode(
                    cip8AddressBytes,
                    "cc_cold"
            );

        };

    }

}
