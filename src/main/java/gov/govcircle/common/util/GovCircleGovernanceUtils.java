package gov.govcircle.common.util;

import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.transaction.spec.governance.DRep;

public class GovCircleGovernanceUtils {
    public static String dRepCip129fromByte(byte[] cip8AddressBytes) {
        String dRepBech32 = dRepIdFromByte(cip8AddressBytes);
        return dRepBech32.getBytes().length != 29
                ? GovId.drepFromKeyHash(Bech32.decode(dRepBech32).data)
                : dRepBech32;

    }
    public static String dRepIdFromByte(byte[] cip8AddressBytes) {
        return Bech32.encode(
                cip8AddressBytes,
                "drep"
        );

    }

}
