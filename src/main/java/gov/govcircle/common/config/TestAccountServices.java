package gov.govcircle.common.config;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.client.crypto.KeyGenCborUtil;
import com.bloxbean.cardano.client.exception.CborDeserializationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TestAccountServices {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Network devkitNetwork = new Network(
            0b0000,
            42
    );
    private static final String minusFiveAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusFiveMnemonic = "organ field install kangaroo chuckle world nuclear sail recipe wood thank forget unlock sunset win endless rhythm visit rifle wait kite strong feed normal";
    private static final Account minusFiveAccount = Account.createFromMnemonic(
            devkitNetwork,
            minusFiveMnemonic
    );
    // MOIRA
    private static final String minusFourAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusFourMnemonic = "matter noise virus patrol ill size raven surface once gentle brief avoid jar model elbow merge safe chicken capable youth bleak bone ginger book";
    private static final Account minusFourAccount = Account.createFromMnemonic(
            devkitNetwork,
            minusFourMnemonic
    );
    private static final String minusThreeAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusThreeMnemonic = "organ field install kangaroo chuckle world nuclear sail recipe wood thank forget unlock sunset win endless rhythm visit rifle wait kite strong feed normal";
    private static final Account minusThreeAccount = Account.createFromMnemonic(
            devkitNetwork,
            minusThreeMnemonic
    );
    private static final String minusTwoAddress = "addr_test1qpdegdt9npj8rjs4yddvhv0h4a5yc76vf55l2vj0x4wl78ypj3mq9n92wxnncpyu9c852glfkprf92k7wm6ya6h2z0ksfj8a0q";
    private static final String minusTwoMnemonic = "picture educate lab rigid puzzle raw forget evidence fiber clerk swim again believe soon loop leisure afraid post run truth couch joke fame pyramid";
    private static final Account minusTwoAccount = Account.createFromMnemonic(
            devkitNetwork,
            minusTwoMnemonic
    );
    private static final String minusAddress = "addr_test1qqf8lfynd9fn46883k0leqj3gvsmrxh5k0xgd6yyfnqgzeedxslupe738ywcqlcyx7pdn07qyvmyvrkanttely8jdhkqfqyzah";
    private static final String minusMnemonic = "lawn decade state sketch equip obscure cannon mechanic ski earth nuclear pizza catalog hen grape slight easily lunch brain since unlock run oven mobile";
    private static final Account minusAccount = Account.createFromMnemonic(
            devkitNetwork,
            minusMnemonic
    );
    private static final String zeroAddress = "addr_test1qpd0ezytxvac588ckaf8szsn2hpwttf04shyutms477xssvap0sj2c3a7amr3mjt6rxdh48ypyfyjxvs3zt7zytjfm5qrct35y";
    private static final String zeroMnemonic = "crouch horse decline milk employ genre lecture window blade split learn slice clip dish long leg end ten fossil patch shield planet artefact beef";
    private static final Account zeroAccount = Account.createFromMnemonic(
            devkitNetwork,
            zeroMnemonic
    );
    private static String dRepId;
    private static final String firstAddress = "addr_test1qryvgass5dsrf2kxl3vgfz76uhp83kv5lagzcp29tcana68ca5aqa6swlq6llfamln09tal7n5kvt4275ckwedpt4v7q48uhex";
    private static final String firstMnemonic = "test test test test test test test test test test test test test test test test test test test test test test test sauce";
    private static final Account firstAccount = Account.createFromMnemonic(
            devkitNetwork,
            firstMnemonic
    );
    private static final String secondAddress = "addr_test1qqkhcxnexjveva2glwl4phmutugledsccwd908lre5q60x6zpyykdlxepldfnmwantnvtw7a65jadpfd3k6hdeytzfzq5s4x5c";
    private static final String secondMnemonic = "dynamic carbon join segment area donate negative differ grape cinnamon husband wool picture home human crater voyage luxury crime flash icon artefact mercy canyon";
    private static final Account secondAccount = Account.createFromMnemonic(
            devkitNetwork,
            secondMnemonic
    );
    private static final String thirdAddress = "addr_test1qpgvqd5xewcml9ayqsvell5vc0rkg6d55n35q3ejmegpkmm3wyze8wq6qy00y97g2r62kccnlaeg3nrlyq6mu3yhlf3qp4v647";
    private static final String thirdMnemonic = "unfair object rubber unusual melt napkin parrot truth manage novel sister student action amount raw divorce tomato awake hire exchange inside slam today area";
    private static final Account thirdAccount = Account.createFromMnemonic(
            devkitNetwork,
            thirdMnemonic
    );
    private static final String forthAddress = "addr_test1qrvl8tf44prseaffk0nf0h0grxq7py5u9hupe4qfve558ptfp5hyq5dtmyr2p8gdjcynmcmylhyu9h6rszj2x4aeunaqxrmc8w";
    private static final String forthMnemonic = "raw toe mobile injury base tube region record unhappy jealous frog inhale mystery wood always group target awesome noble deer edge surround disease daughter";
    private static final Account forthAccount = Account.createFromMnemonic(
            devkitNetwork,
            forthMnemonic
    );
    private static final String fifthAddress = "addr_test1qp7w7369ge0tvfl0v78xu000rn2e87hg7raxh8uhxxtcj8mvh30e4n59ln5sjwyzzxpma4wrn6l8z9862t4dlmhddwhqpx7hmv";
    private static final String fifthMnemonic = "cause sustain bright filter furnace panic route noise account chunk shrimp drink news solution taste monster absurd chronic dice blue recipe next trial green";
    private static final Account fifthAccount = Account.createFromMnemonic(
            devkitNetwork,
            fifthMnemonic
    );

    public static GovcircleSPO createGovcircleSPOFomKeysPath(
            String keysPath
    ) {
        try {
            JsonNode coldSKeyJson = objectMapper.readTree(Files.readString(Path.of(keysPath + "cold.skey")));
            JsonNode coldVKeyJson = objectMapper.readTree(Files.readString(Path.of(keysPath + "cold.vkey")));
            String coldSKeyCborHex = coldSKeyJson
                    .get("cborHex")
                    .asText();
            String coldVKeyCborHex = coldVKeyJson
                    .get("cborHex")
                    .asText();
            byte[] signingKeyBytes = KeyGenCborUtil.cborToBytes(coldSKeyCborHex);
            byte[] verificationKeyBytes = KeyGenCborUtil.cborToBytes(coldVKeyCborHex);
            String firstPoolId = Bech32.encode(
                    Blake2bUtil.blake2bHash224(verificationKeyBytes),
                    "pool"
            );
            return new GovcircleSPO(
                    coldSKeyCborHex,
                    coldVKeyCborHex,
                    signingKeyBytes,
                    verificationKeyBytes,
                    firstPoolId
            );

        } catch (IOException | CborDeserializationException e) {
            throw new RuntimeException(e);

        }

    }

    private static final List<String> mnemonics = List.of(
            fifthMnemonic,
            forthMnemonic,
            thirdMnemonic,
            secondMnemonic,
            firstMnemonic,
            zeroMnemonic,
            minusMnemonic,
            minusTwoMnemonic,
            minusThreeMnemonic,
            minusFourMnemonic,
            minusFiveMnemonic
    );
    private static final List<String> spoKeysPath = List.of(
            "src/main/resources/test-keys-certs/spo/first/",
            "src/main/resources/test-keys-certs/spo/second/",
            "src/main/resources/test-keys-certs/spo/third/"
    );
    public static Map<Integer, GovcircleSPO> govcircleSPOMap = new HashMap<>();
    public static Map<Integer, GovcircleAccount> govcircleAccounMap = new HashMap<>();

    public static void fetchGovcircleAccountList() {
        int accountIndex = 1;
        for (String mnemonic : mnemonics) {
            Account account = Account.createFromMnemonic(
                    devkitNetwork,
                    mnemonic
            );
            String paymentAddress = account
                    .getBaseAddress()
                    .getAddress();
            String cip129DRepId = account.drepId();
            String cip105DRepId = account.legacyDRepId();
            String stakeAddress = account.stakeAddress();
            GovcircleAccount govcircleAccount = new GovcircleAccount(
                    accountIndex,
                    mnemonic,
                    paymentAddress,
                    stakeAddress,
                    cip105DRepId,
                    cip129DRepId,
                    account
            );
            govcircleAccounMap.put(
                    accountIndex++,
                    govcircleAccount
            );

        }
        int spoIndex = 1;
        for (String path : spoKeysPath) {
            GovcircleSPO govcircleSPO = createGovcircleSPOFomKeysPath(path).setIndex(spoIndex);
            govcircleSPOMap.put(
                    spoIndex++,
                    govcircleSPO
            );

        }


    }

    public static Optional<GovcircleAccount> getAccountWithNo(int number) {
        return Optional.ofNullable(
                govcircleAccounMap.get(number)
        );

    }

    public static Optional<GovcircleSPO> getSPOWithNo(int number) {
        return Optional.ofNullable(
                govcircleSPOMap.get(number)
        );

    }

    public static String showGovcircleAccountList() {
        StringBuilder finalResults = new StringBuilder();
        finalResults.append("               *** GOVCIRCLE ACCOUNTS ***              ");
        for (Integer key : govcircleAccounMap.keySet()) {
            GovcircleAccount account = govcircleAccounMap.get(key);
            String stringBuilder = "Index: " + account.getIndex() + "\n" +
                    "Mnemonic: " + account.getMnemonic() + "\n" +
                    "Payment Address: " + account.getPaymentAddress() + "\n" +
                    "Stake Address: " + account.getStakeAddress() + "\n" +
                    "DRep Id (cip105): " + account.getCip105DRepId() + "\n" +
                    "DRep Id (cip129): " + account.getCip129DRepId() + "\n" +
                    "_____________________________________________________" + "\n";
            finalResults.append(stringBuilder);

        }
        finalResults.append("\n");
        finalResults.append("\n");
        finalResults.append("               *** GOVCIRCLE SPOs ***              ");
        for (Integer key : govcircleSPOMap.keySet()) {
            GovcircleSPO spo = govcircleSPOMap.get(key);
            String stringBuilder = "Index: " + spo.getIndex() + "\n" +
                    "SigningKey Hex: " + spo.getSigningKeyHex() + "\n" +
                    "VerificationKey Hex: " + spo.getVerificationKeyHex() + "\n" +
                    "Pool Id: " + spo.getPoolId() + "\n" +
                    "_____________________________________________________" + "\n";
            finalResults.append(stringBuilder);

        }
        finalResults.append("\n");
        finalResults.append("\n");
        finalResults.append(guide());
        return finalResults.toString();

    }

    public static String guide() {
        return "SIGN – Quick Usage Guide\n" +
                "======================================================\n" +
                "\n" +
                "Sign data from a file:\n" +
                "\n" +
                "  sign --data <text-data>\n" +
                "\n" +
                "\n" +
                "Sign with DRep\n" +
                "-------------------------------------------------------\n" +
                "\n" +
                "Use DRep with CIP-105 header:\n" +
                "  sign drep --cip105 --data <text-data>\n" +
                "\n" +
                "Use DRep with CIP-129 header:\n" +
                "  sign drep --cip129 --data <text-data>\n" +
                "\n" +
                "\n" +
                "Sign with SPO\n" +
                "-------------------------------------------------------" +
                "\n" +
                "Use SPO pool id header:\n" +
                "  sign pool --data <text-data>\n" +
                "\n" +
                "\n" +
                "Sign with Wallet addresses\n" +
                "-------------------------------------------------------" +
                "\n" +
                "Use payment address in header:\n" +
                "  sign address --payment --data <text-data>\n" +
                "\n" +
                "Use stake address in header:\n" +
                "  sign address --stake --data <text-data>\n" +
                "\n" +
                "Use a specific address:\n" +
                "  sign address --payment <payment_address> --data <text-data>\n" +
                "  sign address --stake <stake_address> --data <text-data>\n" +
                "\n" +
                "If no address is specified, the first available address of that type is used\n" +
                "\n" +
                "Choose one of the accounts to sign:\n" +
                "  sign address --payment <payment_address> --data <text-data> --account <index>\n" +
                "  sign drep --cip129 --data <text-data> --account <index>\n" +
                "  sign spo --data <text-data> --account <index>\n" +
                "\n" +
                "======================================================\n";

    }


    @Data
    @AllArgsConstructor
    public static class GovcircleAccount {

        private Integer index;
        private String mnemonic;
        private String paymentAddress;
        private String stakeAddress;
        private String cip105DRepId;
        private String cip129DRepId;
        private Account account;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class GovcircleSPO {
        public GovcircleSPO(
                String signingKeyHex,
                String verificationKeyHex,
                byte[] signingKeyBytes,
                byte[] verificationKeyBytes,
                String poolId
        ) {
            this.signingKeyHex = signingKeyHex;
            this.verificationKeyHex = verificationKeyHex;
            this.signingKeyBytes = signingKeyBytes;
            this.verificationKeyBytes = verificationKeyBytes;
            this.poolId = poolId;

        }

        private Integer index;
        private String signingKeyHex;
        private String verificationKeyHex;
        private byte[] signingKeyBytes;
        private byte[] verificationKeyBytes;
        private String poolId;

    }

}
