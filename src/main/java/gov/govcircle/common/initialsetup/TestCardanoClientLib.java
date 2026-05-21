package gov.govcircle.common.initialsetup;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.address.Credential;
import com.bloxbean.cardano.client.api.ProtocolParamsSupplier;
import com.bloxbean.cardano.client.api.UtxoSupplier;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Amount;
import com.bloxbean.cardano.client.api.model.ProtocolParams;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.api.model.Utxo;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.api.DefaultProtocolParamsSupplier;
import com.bloxbean.cardano.client.backend.api.DefaultUtxoSupplier;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.cip.cip20.MessageMetadata;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.COSEKey;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import com.bloxbean.cardano.client.coinselection.UtxoSelectionStrategy;
import com.bloxbean.cardano.client.coinselection.impl.DefaultUtxoSelectionStrategyImpl;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.config.Configuration;
import com.bloxbean.cardano.client.crypto.*;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.function.*;
import com.bloxbean.cardano.client.function.helper.BalanceTxBuilders;
import com.bloxbean.cardano.client.function.helper.InputBuilders;
import com.bloxbean.cardano.client.function.helper.SignerProviders;
import com.bloxbean.cardano.client.governance.GovId;
import com.bloxbean.cardano.client.quicktx.QuickTxBuilder;
import com.bloxbean.cardano.client.quicktx.Tx;
import com.bloxbean.cardano.client.spec.UnitInterval;
import com.bloxbean.cardano.client.transaction.spec.Transaction;
import com.bloxbean.cardano.client.transaction.spec.cert.AuthCommitteeHotCert;
import com.bloxbean.cardano.client.transaction.spec.cert.Certificate;
import com.bloxbean.cardano.client.transaction.spec.cert.PoolRegistration;
import com.bloxbean.cardano.client.transaction.spec.cert.StakePoolId;
import com.bloxbean.cardano.client.transaction.spec.governance.*;
import com.bloxbean.cardano.client.transaction.spec.governance.actions.*;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.client.util.JsonUtil;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.govcircle.common.security.provider.RoleSigner;
import gov.govcircle.common.security.provider.IDParser;
import gov.govcircle.common.security.provider.SignerIdEncoder;
import gov.govcircle.common.security.provider.SignerIdParser;
import gov.govcircle.common.util.GovCircleGovernanceUtils;
import gov.govcircle.common.util.GovCircleSignatureUtils;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

import static com.bloxbean.cardano.client.common.ADAConversionUtil.adaToLovelace;
import static com.bloxbean.cardano.client.crypto.Bech32.encode;
import static com.bloxbean.cardano.client.function.helper.AuxDataProviders.metadataProvider;
import static com.bloxbean.cardano.client.function.helper.BalanceTxBuilders.balanceTx;
import static com.bloxbean.cardano.client.function.helper.InputBuilders.createFromSender;
import static com.bloxbean.cardano.client.function.helper.SignerProviders.signerFrom;
import static com.bloxbean.cardano.yaci.core.util.Constants.LOVELACE;

public class TestCardanoClientLib {

    private static final Network devkitNetwork = new Network(
            0b0000,
            42
    );
    private static final String minusSixthAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusSixthMnemonic = "organ field install kangaroo chuckle world nuclear sail recipe wood thank forget unlock sunset win endless rhythm visit rifle wait kite strong feed normal";
    private static final Account minusSixthAccount = Account.createFromMnemonic(
            Networks.preprod(),
            minusSixthMnemonic
    );

    private static final String minusFiveAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusFiveMnemonic = "organ field install kangaroo chuckle world nuclear sail recipe wood thank forget unlock sunset win endless rhythm visit rifle wait kite strong feed normal";
    private static final Account minusFiveAccount = Account.createFromMnemonic(
            Networks.preprod(),
            minusFiveMnemonic
    );

    // MOIRA
    private static final String minusFourAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusFourMnemonic = "matter noise virus patrol ill size raven surface once gentle brief avoid jar model elbow merge safe chicken capable youth bleak bone ginger book";
    private static final Account minusFourAccount = Account.createFromMnemonic(
            Networks.preprod(),
            minusFourMnemonic
    );

    private static final String minusThreeAddress = "addr_test1qprl3az853lv0f8p7cq2t976r9sdt460xlqryw4t5sgl7ujtxummw52fn40d2zuk55g7mwd2sc7xu472d4ncvr0658rs5h422r";
    private static final String minusThreeMnemonic = "organ field install kangaroo chuckle world nuclear sail recipe wood thank forget unlock sunset win endless rhythm visit rifle wait kite strong feed normal";
    private static final Account minusThreeAccount = Account.createFromMnemonic(
            Networks.preprod(),
            minusThreeMnemonic
    );

    private static final String minusTwoAddress = "addr_test1qpdegdt9npj8rjs4yddvhv0h4a5yc76vf55l2vj0x4wl78ypj3mq9n92wxnncpyu9c852glfkprf92k7wm6ya6h2z0ksfj8a0q";
    private static final String minusTwoMnemonic = "picture educate lab rigid puzzle raw forget evidence fiber clerk swim again believe soon loop leisure afraid post run truth couch joke fame pyramid";
    private static final Account minusTwoAccount = Account.createFromMnemonic(
            Networks.preprod(),
            minusTwoMnemonic
    );

    private static final String minusAddress = "addr_test1qqf8lfynd9fn46883k0leqj3gvsmrxh5k0xgd6yyfnqgzeedxslupe738ywcqlcyx7pdn07qyvmyvrkanttely8jdhkqfqyzah";
    private static final String minusMnemonic = "lawn decade state sketch equip obscure cannon mechanic ski earth nuclear pizza catalog hen grape slight easily lunch brain since unlock run oven mobile";
    private static final Account minusAccount = Account.createFromMnemonic(
            Networks.preprod(),
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


    private static final String sixthMnemonic = "croch horse decline milk employ genre lecture window blade split l";
    private static final Account sixthAccount = Account.createFromMnemonic(
            Networks.preprod(),
            thirdMnemonic
    );

    private static final BackendService backendService = new BFBackendService(
            "http://localhost:8080/api/v1/",
//            "https://cardano-preprod.blockfrost.io/api/v0/",
            "preprodJGVNM6QBJEVExT1qS1chLfKRnM9Ae8Qy"
    );
    public static final QuickTxBuilder quickTxBuilder = new QuickTxBuilder(backendService);

    public static void main(String[] args) throws Exception {

//        byte[] privateKeyByTS = firstAccount.privateKeyBytes().;
//        zeroAccount.get
//        byte[] rootXPrvBytes = zeroAccount
//                .getRootKeyPair()
//                .get()
//                .getPrivateKey().toBech32()
//                .getBytes();
//
//        String base_Address = zeroAccount.getBaseAddress().getAddress();
//
//        String compare = encode(rootXPrvBytes, "xprv");
//        String with = zeroAccount.getRootKeyPair().get().getPrivateKey().toBech32();
////        String str = HexUtil.(privateKeyByTS);
//
//        String dRepPublicKeyHash = "5283b1039baf4e89beaacb595020e381d694e38b4415beee8b7af1e6";
//        String cip129DRep = GovId.drepFromKeyHash(HexUtil.decodeHexString(dRepPublicKeyHash));
//        zeroAccount.hdKeyPair().getPrivateKey().toBech32();
//
//        zeroAccount.hdKeyPair().getPrivateKey().toBech32();

//        SecretKey fromCborHex = new SecretKey("588020dd048be0ca6e8ca4d6010941b1e5dd0fe6a2664daba4ac57920a19bac0f55f6d67092eabeb6d25be8ef2c745b43b7b36f1e45c943eb9a11add7ceae56f2e546a0d6e66e9f336535cc05d5cccc2429f041fca8e7a014c2cbbe4d25e55e0790b3e637890d740b9e12d7ad7942c977ef6be7da5f7f9b4b9764bd2ff3c20b6f609");
//        SecretKey fromPrivateKeyBytes = SecretKey.create(zeroAccount.privateKeyBytes());
//
//        Bech32.Bech32Data data = Bech32.decode("drep122pmzqum4a8gn042edv4qg8rs8tffcutgs2mam5t0tc7vqru52m");
//
//        String nes = Bech32.encode(HexUtil.decodeHexString(dRepPublicKeyHash), "drep");
//        String decoded = HexUtil.encodeHexString(data.data);
//        System.out.println("Result:   " + cip129DRep);
//
//        System.out.println("_______________________________________________");
//
//
//        String signature = "845829a201276761646472657373581cf3549d1c630df3f36e951b44e42ebd02f92241cf7ca17c6a3c030f89a166686173686564f4426d655840034bdb37df19b5f9ea469a7b4d11a6eb2a670583083a7cdb1bd8e98cc4e06f783cb3756c92feb4350e3119b2d552de9272279fcf4745f247a6a88d249153c20f";
//        String key = "a4010103272006215820c233ee453be273921a36887aebf428da40b5cd6f3f226ba6f5e9838c050f3feb";
//
////        String signature = "845846a2012767616464726573735839005afc888b333b8a1cf8b752780a1355c2e5ad2fac2e4e2f70afbc68419d0be125623df77638ee4bd0ccdbd4e409124919908897e111724ee8a166686173686564f4426d655840e1554ba653c714d4ca4b468d716d92014f26e42e01267f85f17989420bcf2bbcc094ad7c936804f77511346d669560a9627944658dd2f35743bd36bd85235100";
////        String key = "a40101032720062158206a0d6e66e9f336535cc05d5cccc2429f041fca8e7a014c2cbbe4d25e55e0790b";
//
//        DataSignature dataSignature = new DataSignature(
//                signature,
//                key
//        );
//        COSEKey coseKey = dataSignature.coseKey();
//        byte[] pubKey = coseKey.otherHeaderAsBytes(-2L);
//        // 2) compute hex of raw pubkey (32 bytes -> 64 hex chars)
//        String pubKeyHex = HexUtil.encodeHexString(pubKey); // lowercase by HexUtil
//        System.out.println(pubKeyHex);
//
//// 3) compute Cardano key-hash = Blake2b-224(pubKey) (28 bytes)
//        byte[] keyHash = Blake2bUtil.blake2bHash224(pubKey);
//        String keyHashHex = HexUtil.encodeHexString(keyHash); // 56 hex chars
//        System.out.println(keyHashHex);
//
//// 4) bech32 of key-hash (HRP used by library for verification-key-hash)
//        String keyHashBech32 = Bech32.encode(keyHash, Address.ADDR_VKH_PREFIX); // e.g. "addr_vkh1..."
//        System.out.println(keyHashBech32);
//
//// 5) optional: bech32 of the raw vkey (if needed)
//        String vkeyBech32 = Bech32.encode(pubKey, "ed25519_pk"); // e.g. "ed25519_pk1..."
//        System.out.println(vkeyBech32);
//
//        VerificationKey k = VerificationKey.create(pubKey);
//
//
//        COSESign1 frontEndSignature = dataSignature.coseSign1();
//
////        byte[] addressBytes = frontEndSignature
////                .headers()
////                ._protected()
////                .getAsHeaderMap()
////                .otherHeaderAsBytes("address");
////        Address address = new Address(addressBytes);
////        String bech32Address = address.toBech32();
//
//        COSESign1 coseSign1 = dataSignature.coseSign1();
//        byte[] addressBytes = coseSign1.headers()._protected().getAsHeaderMap().otherHeaderAsBytes("address");
//        byte[] addressBytes2 = dataSignature.address();
//
//        SignerIdParser.SignerId signer = SignerIdParser.parseSignerId(addressBytes);
//        IDParser.SignerId id = IDParser.parseSignerId(addressBytes);
//        String dRepID;
//        if (signer != null) {
//            if (signer.address != null) {
//                // signed with a normal payment/stake address
//                Address address = signer.address;
//
//            } else {
//                // signed with DRep
//                DRep drep = signer.drep;
//                String bech32 = signer.drepBech32;
//
//            }
//
//        } else {
//            // unknown type — handle gracefully
//
//        }
//        String cColdKey = "5820e869413154655c170016abb1b896b1e0b026faf5c12dc57a3746d2fcbf02f50f";
////        byte[] pks = KeyGenCborUtil.cborToBytes(cColdKey);
//        byte[] cborBytesss = HexUtil.decodeHexString(cColdKey);
//
//        byte[] pks = Arrays.copyOfRange(
//                cborBytesss,
//                2,
//                cborBytesss.length
//        );
//        byte[] pksKeyHash = Blake2bUtil.blake2bHash224(pks);
//
//        String ssdfs = GovId.ccColdFromKeyHash(pksKeyHash);
//
//
//        DataSignature dsfss = new DataSignature();
//        dsfss.signature("84582aa201276761646472657373581d12732010e59095172a0d98f7d6944724bed4920b3f5eec936ec9fc7faba166686173686564f44568656c6c6f584020351b63f1d5a595bfa6197ba6003b80c1bcf72fbf92f437ef74088b00be6c020469597121fb836822c60606962ed6b2d4d04a83d56055411731bb3223ec8c0d");
//        dsfss.key("a4010103272006215820e869413154655c170016abb1b896b1e0b026faf5c12dc57a3746d2fcbf02f50f");
//        String payload = new String(dsfss.coseSign1().payload());
//        byte[] sssss = dsfss.coseSign1().headers()._protected().getAsHeaderMap().otherHeaderAsBytes("address");
//        String ccColdBech32 = Bech32.encode(sssss, "cc_cold");
//        Credential cred = GovId.ccColdToCredential(ccColdBech32);
//
//
//        DataSignature dsf = new DataSignature();
//        dsf.signature("845846a20127676164647265737358390085c47bdca9d053631adf686e10dd338cc80b72e992ddcade056032c99d0be125623df77638ee4bd0ccdbd4e409124919908897e111724ee8a166686173686564f4426d655840bbdad6c5e5f42adc3cb265a26fd6c29a3b870847072b216000d2cc0ca355d645b98ba9ea25cebc37ec4e0056a34c8acb45d3f5963e0260e881f0f495fea3e900");
//        dsf.key("a4010103272006215820fdf735b0e9bd1d3075694ea966731c18e5d53c3b29e0b10f6a3bfd49eb1c779d");
//        CIP30DataSigner.INSTANCE.verify(dsf);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode coldVKeyJson = objectMapper.readTree(Files.readString(Path.of("src/main/resources/test-keys-certs/spo/third/cold.vkey")));
//        String coldVKeyCborHex = coldVKeyJson.get("cborHex").asText();
//        JsonNode coldSKeyJson = objectMapper.readTree(Files.readString(Path.of("src/main/resources/test-keys-certs/spo/third/cold.skey")));
//        String coldSKeyCborHex = coldSKeyJson.get("cborHex").asText();
//        byte[] addressBytesAbstracted = RoleSigner.bech32Decode("pool1ff6d455l8ypfjzuc4fcmjz6cx29atyufzgstqfev39tsy0y9c2n")._2;
//        byte[] addressBytesAbstractedIt = SignerIdEncoder.poolBech32ToBytes("pool1ff6d455l8ypfjzuc4fcmjz6cx29atyufzgstqfev39tsy0y9c2n");
//        byte[] pk = KeyGenCborUtil.cborToBytes(coldVKeyCborHex);
//        byte[] pkWithHexUtil = HexUtil.decodeHexString(coldVKeyCborHex);
//
//        VerificationKey poolColdVerificationKey = new VerificationKey(coldVKeyCborHex);
//        String poolColdVerificationKeyHash = KeyGenUtil.getKeyHash(poolColdVerificationKey);
//        byte[] poolColdVerificationKeyHashBytesss = poolColdVerificationKeyHash.getBytes(StandardCharsets.UTF_8);
//        byte[] poolColdVerificationKeyHashBytess = poolColdVerificationKeyHash.getBytes();
//        byte[] poolColdVerificationKeyHashBytes = Blake2bUtil.blake2bHash224(pk);
//        String poolColdVerificationKeyHashWithHexUtils = HexUtil.encodeHexString(poolColdVerificationKeyHashBytes);
//
//
//        byte[] sk = KeyGenCborUtil.cborToBytes(coldSKeyCborHex);
//        DataSignature ds = RoleSigner.signForRole(
//                "payLoadBytes".getBytes(),
//                sk,
//                pk,
//                addressBytesAbstracted,
//                true
//        ); // true if you want hashed payload behavior
//        System.out.println("holt");
////        CIP30DataSigner.INSTANCE.verify()
//
//
////        CIP30DataSigner.INSTANCE.signData();
//        COSEKey coseKey1 = dataSignature.coseKey();
//
//        DataSignature ss = CIP30DataSigner.INSTANCE.signData(
//                addressBytesAbstracted,
//                "SPO I agree with terms e7a34478-8427-4f17-acc2-1fea877c7ab3".getBytes(StandardCharsets.UTF_8),
//                sk,
//                pk
//        );
//        DataSignature sf = CIP30DataSigner.INSTANCE.signData(
//                addressBytesAbstractedIt,
//                "SPO I agree with terms e7a34478-8427-4f17-acc2-1fea877c7ab3".getBytes(StandardCharsets.UTF_8),
//                sk,
//                pk
//        );
//        DataSignature blomberg = CIP30DataSigner.INSTANCE.signData(
//                Bech32.decode(zeroAccount.drepId()).data,
//                "DREP:I agree with terms:27314dbd-ed82-48c5-bd84-304626baf476".getBytes(StandardCharsets.UTF_8),
//                zeroAccount.drepKey().signingKey(),
//                zeroAccount.drepKey().verificationKey()
//        );
        DataSignature fr = new DataSignature(
                "84584da301270458202b41abe97d5c84f30691740cb564305fd3f30514777e56581fd0bef02a92e29d6761646472657373581de0f8ed3a0eea0ef835ffa7bbfcde55f7fe9d2cc5d55ea62cecb42bab3ca166686173686564f4583e57414c4c45543a492061677265652077697468207465726d733a34323364636131622d343739302d343864662d626265372d3766653031656232383535345840027cc82e02b1332dc0e684c6ba99019f00691c1c4028ad847740c22bd60ed82c9ace070fdc37933ba4c89ea18c67e547fe0d7a2e41b3f30f34f75cd887877108",
                "a40101032720062158202b41abe97d5c84f30691740cb564305fd3f30514777e56581fd0bef02a92e29d"
        );
        DataSignature blombergious = CIP30DataSigner.INSTANCE.signData(
                Bech32.decode(
                        zeroAccount
                                .getBaseAddress()
                                .getAddress()
                )
                        .data,
                "WALLET:I agree with terms:423dca1b-4790-48df-bbe7-7fe01eb28554".getBytes(StandardCharsets.UTF_8),
                zeroAccount.privateKeyBytes(),
                zeroAccount.publicKeyBytes()
        );
        String frIdentifier = GovCircleSignatureUtils.getKeyIdentifierFromDataSignature(fr);
        String frPayload = GovCircleSignatureUtils.getDataSignaturePayload(fr);
        System.out.println("Holt HEre");
//        String ssss = blomberg.signature();
//        String kkkk = blomberg.key();
//        DataSignature cardanoDataSignerPool = new DataSignature(
//                "845829a201276761646472657373581c4a74dad29f3902990b98aa71b90b58328bd593891220b0272c895702a166686173686564f4465a6f687265485840758d0bb6714375bbb0e05122e83d1b7c9a63821b71a547cfde0769b5e4dd7aaa83e698af2703585d56fb7a09048b2fa10b605bc6086827ff397b86795843af0d",
//                "a40101032720062158203f08abc62bea050c4851eb5415592959a8fd848dfe275834d911fa04a2e2b9b4"
//        );
//
//        byte[] pubKey1 = coseKey1.otherHeaderAsBytes(-2L);
//        // 2) compute hex of raw pubkey (32 bytes -> 64 hex chars)
//        String pubKeyHex1 = HexUtil.encodeHexString(pubKey1); // lowercase by HexUtil
//        System.out.println(pubKeyHex1);
//
//// 3) compute Cardano key-hash = Blake2b-224(pubKey) (28 bytes)
//        byte[] keyHash1 = Blake2bUtil.blake2bHash224(pubKey1);
//        String keyHashHex1 = HexUtil.encodeHexString(keyHash1); // 56 hex chars
//        System.out.println(keyHashHex1);






// 6) What is stored in the entity?
//        String dRepHashStored = dRepEntity.getDRepHash(); // adapt to your getter



//        updateCommittee(firstAccount, null);
//        registerCommittee(firstAccount);

        String dRepId = registerDRep(firstAccount);

        // a4010103272006215820fe17644f7d3095805715d1e042e10cc8fe3991c81cc9cbb2b2d0cacbb0ec14f2
        DataSignature fron = new DataSignature(
                "84584da30127045820fe17644f7d3095805715d1e042e10cc8fe3991c81cc9cbb2b2d0cacbb0ec14f26761646472657373581d225283b1039baf4e89beaacb595020e381d694e38b4415beee8b7af1e6a166686173686564f4583c445245503a492061677265652077697468207465726d733a32373331346462642d656438322d343863352d626438342d3330343632366261663437365840d3592aeee8403446da10389499c07438a692009f610956cfbd5588418aa848f4e2ecfc55204bdacd99e732217e57f4f566723661605082de568409a26514990d",
                "a4010103272006215820fe17644f7d3095805715d1e042e10cc8fe3991c81cc9cbb2b2d0cacbb0ec14f2"
        );

        DataSignature blomberg = CIP30DataSigner.INSTANCE.signData(
                Bech32.decode(
                        firstAccount.drepId()).data,
                "DREP:I agree with terms:27314dbd-ed82-48c5-bd84-304626baf476".getBytes(StandardCharsets.UTF_8),
                firstAccount
                        .drepKey()
                        .signingKey(),
                firstAccount
                        .drepKey()
                        .verificationKey()
        );

        byte[] cip8AddressBytes = GovCircleSignatureUtils.getCip8AddressBytesFromDataSignature(fron);
        byte[] publicKeyHash = GovCircleSignatureUtils.getCip8PublicKeyHashFromDataSignature(fron);
        String keyIdentifier = GovCircleGovernanceUtils.dRepCip129fromByte(cip8AddressBytes);

        System.out.println(blomberg);
        System.out.println("Hold DRep");


//        System.out.println(backendService.getEpochService().getProtocolParameters());

//        registerPool(
//                firstAccount,
//                secondAccount, firstAccount
//        );
//        firstAccount.committeeColdKey();
//        voteToProposalSPO(
//                forthAccount,
//                "src/main/resources/test-keys-certs/spo/second/cold.vkey",
//                "30d20ae81b4d1a4f4081ca8a85baa710de939ec2e34bec64167126b530eb9f7d",
//                0
//        );
//        createProposal(firstAccount);
//        String dRepId = registerDRep(firstAccount);

//        sendAda(firstAccount, secondAddress);

//        sendAda(firstAccount, thirdAddress);

//        registerStakeAddress(minusTwoAccount);

//        registerStakeAddress(minusThreeAccount);

//        registerStakeAddress(minusFourAccount);

//        registerStakeAddress(firstAccount);

//        registerStakeAddress(secondAccount);

//        registerStakeAddress(thirdAccount);

//        registerStakeAddress(forthAccount);

//        registerStakeAddress(fifthAccount);

//        System.out.println(secondAccount.getBech32PrivateKey());
//        DRep dRep = GovId.toDrep("drep1yte4f8guvvxl8umwj5d5fepwh5p0jgjpea72zlr28spslzgv2eexu");
//        DRep dRep = DRep.abstain();
//        delegateVotingPowerToDRep(
//                zeroAccount,
//                dRep
//        );

//        delegateVotingPowerToDRep(
//                thirdAccount,
//                dRep
//        );

    }

//732010e59095172a0d98f7d6944724bed4920b3f5eec936ec9fc7fab
//732010e59095172a0d98f7d6944724bed4920b3f5eec936ec9fc7fab
    private static String registerDRep(Account account) {
        String baseAddress = account.baseAddress();

        Tx drepRegTx = new Tx()
                .registerDRep(account)
                .from(baseAddress);

        Result<String> drepRegTxResult = quickTxBuilder.compose(drepRegTx)
                .withSigner(SignerProviders.signerFrom(account))
                .withSigner(SignerProviders.signerFrom(account.drepHdKeyPair()))
                .completeAndWait(s -> System.out.println("DRepRegistration --> " + s));

        System.out.println("finished DRepRegistration |->::... " + drepRegTxResult.getResponse());
        System.out.println("<_______________________________>");

        return null;

    }

    private static void delegateVotingPowerToDRep(
            Account account,
            DRep dRep
    ) {
        String baseAddress = account.baseAddress();
        Tx delegationVoteTransaction = new Tx()
                .delegateVotingPowerTo(
                        baseAddress,
                        dRep
                )
                .from(baseAddress);

        Result<String> resultDelegationVoteTransaction = quickTxBuilder.compose(delegationVoteTransaction)
                .withSigner(SignerProviders.signerFrom(account))
                .withSigner(SignerProviders.stakeKeySignerFrom(account))
                .completeAndWait(s -> System.out.println("DelegationVote --> " + s));

        System.out.println("finished DelegationVote |->::... " + resultDelegationVoteTransaction.getResponse());
        System.out.println("<_______________________________>");

    }

    private static void registerStakeAddress(Account account) {

        String baseAddress = account.baseAddress();
        String stakeAddress = account.stakeAddress();

        Tx registerStakeAddressTx = new Tx()
                .registerStakeAddress(stakeAddress)
                .from(baseAddress);

        Result<String> registerStakeAddress = quickTxBuilder.compose(registerStakeAddressTx)
                .withSigner(SignerProviders.signerFrom(account))
                .withSigner(SignerProviders.stakeKeySignerFrom(account))
                .completeAndWait(s -> System.out.println("RegisterStakeAddress --> " + s));

        System.out.println("finished RegisterStakeAddress |->::... " + registerStakeAddress.getResponse());
        System.out.println("<_______________________________>");

    }

    public static void LogAccountData(Account account) {
        String mnemonicGet = account.mnemonic();
        System.out.println("mnemonic :: -> " + mnemonicGet);

        String address = account.baseAddress();
        System.out.println("address :: -> " + address);

    }

    public static void sendAda(
            Account senderAccount,
            String receiverAddress
    ) throws CborSerializationException, ApiException {

        String senderAddress = senderAccount.baseAddress();
        //Addresses to receive ada
//        String receiverAddress1 = "addr_test1qqc0vmcxezfytntua045qzlrfj86erdqgww2d2ennnp09q9f83gezl004gqwyaf8tld2ppe3zulge9v6eq6c9846mx9sphamg4";

        Output output1 = Output.builder()
                .address(receiverAddress)
                .assetName(LOVELACE)
                .qty(adaToLovelace(10))
                .build();
        Output output2 = Output.builder()
                .address(receiverAddress)
                .assetName(LOVELACE)
                .qty(adaToLovelace(20))
                .build();

        MessageMetadata metadata = MessageMetadata.create()
                .add("First transfer transaction");

        TxBuilder txBuilder = output1.outputBuilder()
                .and(output2.outputBuilder())
                .buildInputs(
                        createFromSender(
                                senderAddress,
                                senderAddress
                        )
                )
                .andThen(metadataProvider(metadata))
                .andThen(
                        balanceTx(
                                senderAddress,
                                1
                        )
                );
        UtxoSupplier utxoSupplier = new DefaultUtxoSupplier(backendService.getUtxoService());
        ProtocolParamsSupplier protocolParamsSupplier = new DefaultProtocolParamsSupplier(backendService.getEpochService());

        Transaction signedTransaction = TxBuilderContext.init(
                        utxoSupplier,
                        protocolParamsSupplier
                )
                .buildAndSign(
                        txBuilder,
                        signerFrom(senderAccount)
                );

        Result<String> result = backendService
                .getTransactionService()
                .submitTransaction(signedTransaction.serialize());
        System.out.println(result);
        System.out.println("<_______________________________>");

    }

    public static void registerPool(
            Account signer,
            Account... ownerAccounts
    ) throws Exception {
        ExtractKey coldExtractKey = loadColdKeyHash(
                "src/main/resources/test-keys-certs/spo/second/cold.vkey",
                "src/main/resources/test-keys-certs/spo/second/cold.skey"
        );
        byte[] coldKey28 = coldExtractKey.keyBytes;

        ExtractKey vrfExtractKey = loadVrfKeyHash(
                "src/main/resources/test-keys-certs/spo/second/vrf.vkey",
                "src/main/resources/test-keys-certs/spo/second/vrf.skey"
        );
        byte[] vrfKey32 = vrfExtractKey.keyBytes;
        Set<String> owners = Arrays
                .stream(ownerAccounts)
                .map(
                        acc -> HexUtil.encodeHexString(
                                acc
                                        .stakeHdKeyPair()
                                        .getPublicKey()
                                        .getKeyHash()
                        )

                )
                .collect(Collectors.toSet());

        System.out.println("Stake address bech32: " + signer.stakeAddress());
        byte[] rewardBytes = signer
                .stakeAddress()
                .getBytes();
        System.out.println("RewardAccount hex len: " + rewardBytes.length + " bytes");


        String rewardAccountHex = HexUtil.encodeHexString(rewardBytes);
        System.out.println("Reward account hex: " + rewardAccountHex);

        Address stakeAddr = new Address(signer.stakeAddress());  // parses bech32
        byte[] stakeAddrBytes = stakeAddr.getBytes();              // ✅ binary bytes
        String rewardAccountHex1 = HexUtil.encodeHexString(stakeAddrBytes);

        System.out.println("Reward account length: " + stakeAddrBytes.length + " bytes");
        System.out.println("Reward account hex: " + rewardAccountHex1);

        PoolRegistration poolReg = PoolRegistration.builder()
                .operator(coldKey28)
                .vrfKeyHash(vrfKey32)
                .pledge(BigInteger.valueOf(500000000)) // 500 ADA
                .cost(BigInteger.valueOf(340000000))   // 340 ADA min pool cost
                .margin(new UnitInterval(BigInteger.ONE, BigInteger.valueOf(100L)))      // 1%
                .rewardAccount(HexUtil.encodeHexString(stakeAddrBytes))
                .poolOwners(owners)
                .build();

        Tx tx = new Tx()
                .from(signer.baseAddress())
                .registerPool(poolReg);

        QuickTxBuilder quickTxBuilder = new QuickTxBuilder(backendService);

        QuickTxBuilder.TxContext txContext = quickTxBuilder.compose(tx)
                .withSigner(SignerProviders.signerFrom(coldExtractKey.signingKey));
        for (Account account : ownerAccounts) {
            txContext = txContext
                    .withSigner(SignerProviders.stakeKeySignerFrom(account))
                    .withSigner(SignerProviders.signerFrom(account));

        }

        System.out.println("<------------------->");
        Transaction builtTx = txContext.build();
        System.out.println("Built TX (JSON): " + JsonUtil.getPrettyJson(builtTx));

// inspect outputs and certificates
        System.out.println("Outputs:");
        builtTx.getBody().getOutputs().forEach(o -> System.out.println(o.getAddress() + " -> " + o.getValue().getCoin()));
        System.out.println("Certificates:");
        if (builtTx.getBody().getCerts() != null)
            builtTx.getBody().getCerts().forEach(c -> System.out.println(c));

//        Transaction signedTx = txContext.buildAndSign(); // or build(); then signers.sign(signedTx)
//        byte[] txBytes = Serializer.serialize(signedTx); // use library serializer
//        String txHex = HexUtil.encodeHexString(txBytes);
//        System.out.println("SIGNED_TX_HEX=" + txHex);
        txContext.completeAndWait(s -> System.out.println("Stake pool registration is here --> " + s));

        System.out.println("Done.");

    }
//                .withSigner(SignerProviders.signerFrom(ownerStakeHdKeyPair))                // stake key witness
//      6d3274321c4277b19bc4460f40c7dc7f7ffdf5bb3108d10355b2fb08363105ee

//    b60ed53a5bbf9e8731ca0be9f1642c3e03827db05569c7488272819fe2cea767


    public static ExtractKey loadColdKeyHash(
            String coldVKeyPath,
            String coldSKeyPath
    ) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode coldVKeyJson = mapper.readTree(Files.readString(Path.of(coldVKeyPath)));
        String coldVKeyCborHex = coldVKeyJson.get("cborHex").asText();
        JsonNode coldSKeyJson = mapper.readTree(Files.readString(Path.of(coldSKeyPath)));
        String coldSKeyCborHex = coldSKeyJson.get("cborHex").asText();

        // Compute the key hash
        byte[] coldKeyBytes = KeyGenCborUtil.cborToBytes(coldVKeyCborHex);
        String coldKeyHashHex = KeyGenUtil.getKeyHash(coldKeyBytes);
        byte[] coldKeyBytes32 = HexUtil.decodeHexString(coldKeyHashHex); // 28 bytes

        return ExtractKey.of(
                coldKeyBytes32,
                new SecretKey(coldSKeyCborHex)
        );

    }

    public static ExtractKey loadVrfKeyHash(
            String vrfVKeyPath,
            String vrfSKeyPath
    ) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode vrfVKeyJson = mapper.readTree(Files.readString(Path.of(vrfVKeyPath)));
        String vrfVKeyCborHex = vrfVKeyJson.get("cborHex").asText();
        JsonNode vrfSKeyJson = mapper.readTree(Files.readString(Path.of(vrfSKeyPath)));
        String vrfSKeyCborHex = vrfSKeyJson.get("cborHex").asText();

        // VRF key hash = blake2b_256(CBOR bytes)
        byte[] vrfKeyBytes = KeyGenCborUtil.cborToBytes(vrfVKeyCborHex);
        byte[] vrfKeyBytes32 = Blake2bUtil.blake2bHash256(vrfKeyBytes); // 32 bytes

        return ExtractKey.of(
                vrfKeyBytes32,
                new SecretKey(vrfSKeyCborHex)
        );
    }

    public static void createProposal(
            Account account
    ) throws Exception {
        GovAction govAction = new InfoAction();
        Anchor anchor = new Anchor(
                "https://github.com/Sadeghg/cardano-preprod-proposals/blob/main/treasury_proposal_1.json",
                computeSha256Hex("src/main/resources/proposals-anchor-json/treasury_proposal_1.json")
        );
        Tx tx = new Tx()
                .createProposal(
                        govAction,
                        account.stakeAddress(),
                        anchor
                )
                .from(
                        account
                                .getBaseAddress()
                                .getAddress()
                );

        Result<String> proposalCreation = quickTxBuilder.compose(tx)
                .withSigner(SignerProviders.stakeKeySignerFrom(account))
                .withSigner(SignerProviders.signerFrom(account))
                .completeAndWait(s -> System.out.println("Proposal being created |->::... " + s));
        System.out.println("finished ProposalCreation |->::... " + proposalCreation.getResponse());
        System.out.println("<_______________________________>");

    }

    public static void updateCommittee(
            Account account,
            GovActionId prevGovActionId
    ) throws Exception {

        Credential newMember = Credential.fromKey(
                getKeyHash("src/main/resources/test-keys-certs/cc/first/cc-cold.vkey")
        );

        Map<Credential,Integer> newMembers = new LinkedHashMap<>();
        newMembers.put(newMember, 180);

        UpdateCommittee uc = UpdateCommittee.builder()
                .prevGovActionId(prevGovActionId)
                .membersForRemoval(Collections.emptySet())
                .newMembersAndTerms(newMembers)
                .quorumThreshold(new UnitInterval(BigInteger.ZERO, BigInteger.ONE))
                .build();

//        GovAction govAction = new UpdateCommittee();
        Anchor anchor = new Anchor(
                "https://github.com/Sadeghg/cardano-preprod-proposals/blob/main/treasury_proposal_1.json",
                computeSha256Hex("src/main/resources/proposals-anchor-json/treasury_proposal_1.json")
        );
        Tx tx = new Tx()
                .createProposal(
                        uc,
                        account.stakeAddress(),
                        anchor
                )
                .from(
                        account
                                .getBaseAddress()
                                .getAddress()
                );

        Result<String> proposalCreation = quickTxBuilder.compose(tx)
                .withSigner(SignerProviders.stakeKeySignerFrom(account))
                .withSigner(SignerProviders.signerFrom(account))
                .completeAndWait(s -> System.out.println("Proposal being created |->::... " + s));
        System.out.println("finished ProposalCreation |->::... " + proposalCreation.getResponse());
        System.out.println("<_______________________________>");

    }

    public static void voteToProposalSPO(
            Account account,
            String coldVKeyPath,
            String govIdTxHash,
            Integer govIdTxIndex
    ) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode vrfVKeyJson = mapper.readTree(
                Files.readString(
                        Path.of(
                                coldVKeyPath
                        )
                )
        );
        String vrfVKeyCborHex = vrfVKeyJson.get("cborHex").asText();

        VerificationKey verificationKey = new VerificationKey(vrfVKeyCborHex);
        String coldKeyHashHex = KeyGenUtil.getKeyHash(verificationKey);
        Credential coldVKey = Credential.fromKey(coldKeyHashHex);

        Voter poolVoter = Voter.builder()
                .type(VoterType.STAKING_POOL_KEY_HASH)
                .credential(coldVKey)
                .build();

        GovActionId govActionId = new GovActionId(
                govIdTxHash,
                govIdTxIndex
        );
        Tx tx = new Tx()
                .createVote(
                        poolVoter,
                        govActionId,
                        Vote.YES
                )
                .from(
                        account
                                .getBaseAddress()
                                .getAddress()
                );


        ExtractKey coldExtractKey = loadColdKeyHash(
                "src/main/resources/test-keys-certs/spo/second/cold.vkey",
                "src/main/resources/test-keys-certs/spo/second/cold.skey"
        );
        TxSigner poolSigner = SignerProviders.signerFrom(coldExtractKey.signingKey);
        TxSigner accountStakeSigner = SignerProviders.stakeKeySignerFrom(account);

        Result<String> proposalCreation = quickTxBuilder.compose(tx)
                .withSigner(poolSigner)
                .withSigner(accountStakeSigner)
                .withSigner(SignerProviders.signerFrom(account))
                .completeAndWait(s -> System.out.println("Vote is being casted |->::... " + s));
        System.out.println("finished casting vote |->::... " + proposalCreation.getResponse());
        System.out.println("<_______________________________>");


    }

    public static void voteToProposalDRep() {

    }

    public static void voteToProposalCC() {

    }



    public static byte[] computeSha256Hex(String filePath) throws Exception {
        byte[] jsonBytes = Files.readAllBytes(Path.of(filePath));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(jsonBytes);
        String hashHex = HexUtil.encodeHexString(hashBytes);  // gives lower-case hex
        if(hashBytes.length != 32) {
            throw new IllegalStateException("Hash length is not 32 bytes");

        }
        return hashBytes;

    }

    /**
     * Register CC (create hot-key auth cert and submit).
     *
     * @param ccAccount Account that holds:
     *                  - payment key / baseAddress() with funds
     *                  - committee cold key (CIP-105 path)
     *                  - committee hot key (CIP-105 path)
     */
    public static void registerCommittee(Account ccAccount) throws Exception {
        // 1) Basic inputs
        String paymentAddr = ccAccount.baseAddress();  // change + fee payer

        // --- UTxOs + protocol params ---
        DefaultUtxoSupplier utxoSupplier =
                new DefaultUtxoSupplier(backendService.getUtxoService());
        ProtocolParams protocolParams =
                new DefaultProtocolParamsSupplier(backendService.getEpochService())
                        .getProtocolParams();

        // Select some UTxOs – ask for e.g. 5 ADA, exact amount will be balanced later
        UtxoSelectionStrategy selectionStrategy =
                new DefaultUtxoSelectionStrategyImpl(utxoSupplier);
        Set<Utxo> utxos = selectionStrategy.select(
                paymentAddr,
                new Amount(LOVELACE, adaToLovelace(5)),   // “enough for fees”
                Collections.emptySet()
        );
        if (utxos == null || utxos.isEmpty())
            throw new IllegalStateException("No UTxO found for " + paymentAddr);

        // 2) Build the AuthCommitteeHotCert (what CLI writes to hot-auth.cert)
        Credential coldCredential = Credential.fromKey(
                getKeyHash("src/main/resources/test-keys-certs/cc/first/cc-cold.vkey")
        );
//        HexUtil.encodeHexString(getKeyHash("src/main/resources/test-keys-certs/cc/first/cc-cold.vkey"))    hexString
        Credential hotCredential = Credential.fromKey(
                getKeyHash("src/main/resources/test-keys-certs/cc/first/cc-hot.vkey")
        );
        AuthCommitteeHotCert authCert = new AuthCommitteeHotCert(
                coldCredential,
                hotCredential
        );

        TxOutputBuilder txOutBuilder = (ctx, outputs) -> {
            // no explicit outputs: reuse existing outputs from InputBuilders + balanceTx
        };

        final int witnessCount = 2; // payment + cc-cold (same as --witness-override 2)

        TxBuilder builder = txOutBuilder
                .buildInputs(
                        InputBuilders.createFromUtxos(
                                new ArrayList<>(utxos),
                                paymentAddr               // change address
                        )
                )
                .andThen((ctx, txn) -> {
                    // attach the CC hot-key authorization cert
                    List<Certificate> certs = txn.getBody().getCerts();
                    if (certs == null) {
                        certs = new ArrayList<>();
                        txn.getBody().setCerts(certs);
                    }
                    certs.add(authCert);
                })
                .andThen(BalanceTxBuilders.balanceTx(paymentAddr, witnessCount));
        ExtractKey coldExtractKey = loadColdKeyHash(
                "src/main/resources/test-keys-certs/cc/first/cc-cold.vkey",
                "src/main/resources/test-keys-certs/cc/first/cc-cold.skey"
        );

        TxSigner ccColdSigner = SignerProviders.signerFrom(coldExtractKey.signingKey);

        // 4) Signers: payment key + CC cold key (NO hot key here)
        TxSigner signer = SignerProviders
                        .signerFrom(ccAccount)                     // payment
                        .andThen(ccColdSigner);                           // cc-cold

        // 5) Build, sign, submit
        Transaction signedTx = TxBuilderContext
                .init(utxoSupplier, protocolParams)
                .buildAndSign(builder, signer);

        Result<String> submitResult = backendService
                .getTransactionService()
                .submitTransaction(signedTx.serialize());
        System.out.println("cc registration |->::... " + submitResult.getResponse());
        System.out.println("<_______________________________>");

    }

    public static byte[] getKeyHash(
            String keyPath
    ) throws IOException {
        String vkeyJson = Files.readString(Path.of(keyPath));
        JsonNode node = new ObjectMapper().readTree(vkeyJson);

        String cborHex = node.get("cborHex").asText();
        byte[] cborBytes = HexUtil.decodeHexString(cborHex);

        byte[] pubKey = Arrays.copyOfRange(
                cborBytes,
                2,
                cborBytes.length
        );
        return Blake2bUtil.blake2bHash224(pubKey);

    }


    public static boolean verifyDRepOwnership(
            String bech32DRepId,
            String bech32DRepVk,
            String nonce,
            String hexSignature
    ) throws Exception {

        // 1. Decode DRep verification key from bech32 ("drep_vk...")
        Bech32.Bech32Data decodedVk = Bech32.decode(bech32DRepVk);
        byte[] pubKey = decodedVk.data; // 32-byte ed25519 vkey

        ExtractKey vrfExtractKey = loadVrfKeyHash(
                "src/main/resources/test-keys-certs/cc/first/vrf.vkey",
                "src/main/resources/test-keys-certs/cc/first/vrf.skey"
        );
        pubKey = vrfExtractKey.keyBytes;

        // 2. Check that this pubKey really corresponds to the claimed DRep ID (CIP-129)
        byte[] keyHash = getKeyHash("src/main/resources/test-keys-certs/cc/first/cc-cold.vkey");
        String expectedDRepId = GovId.drepFromKeyHash(keyHash);
//        if (!expectedDRepId.equals(bech32DRepId)) {
//      //       The key does not belong to the claimed DRep
//            return false;
//        }

        // 3. Rebuild the exact message that was signed
        String message = nonce + "|" + bech32DRepId;
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // 4. Decode signature
        byte[] sigBytes = HexUtil.decodeHexString(hexSignature);

        // 5. Verify signature using the global SigningProvider
        boolean sigOk = Configuration
                .INSTANCE
                .getSigningProvider()
                .verify(
                        sigBytes,
                        messageBytes,
                        pubKey
                );
        return sigOk;


    }


    public static boolean verifyStakePoolOwnership(
            String bech32PoolId,
            String nonce,
            String hexSignature
    )
            throws Exception {

        // 1. Decode pool cold verification key from bech32 ("pool_vk...")
//        Bech32.Bech32Data decoded = Bech32.decode(bech32PoolColdVk);  //bech32PoolColdVk String
//        byte[] coldVkBytes = decoded.data;

        ExtractKey vrfExtractKey = loadVrfKeyHash(
                "src/main/resources/test-keys-certs/cc/first/vrf.vkey",
                "src/main/resources/test-keys-certs/cc/first/vrf.skey"
        );
        byte[] coldVkBytes = vrfExtractKey.keyBytes;

        // 2. Wrap into VerificationKey so StakePoolId helper can use it
        VerificationKey coldVk = VerificationKey.create(coldVkBytes);

        // 3. Recompute pool ID from the cold vkey and compare
        StakePoolId stakePoolId = StakePoolId.fromColdVKey(coldVk);
        String expectedPoolId = stakePoolId.getBech32PoolId();
//        if (!expectedPoolId.equals(bech32PoolId)) {
//            // The key is not the cold key for this pool
//            return false;
//        }

        // 4. Rebuild signed message
        String message = nonce + "|" + bech32PoolId;
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // 5. Decode signature and verify
        byte[] sigBytes = HexUtil.decodeHexString(hexSignature);

        boolean sigOk = Configuration
                .INSTANCE
                .getSigningProvider()
                .verify(
                        sigBytes,
                        messageBytes,
                        coldVkBytes
                );
        return sigOk;
        

    }

    static class ExtractKey {
        public ExtractKey(
                byte[] keyBytes,
                SecretKey signingKey
        ) {
            this.keyBytes = keyBytes;
            this.signingKey = signingKey;

        }

        byte[] keyBytes;
        SecretKey signingKey;

        public static ExtractKey of(
                byte[] keyBytes,
                SecretKey signingKey
        ) {
            return new ExtractKey(
                    keyBytes,
                    signingKey
            );


        }


    }


}


//c233ee453be273921a36887aebf428da40b5cd6f3f226ba6f5e9838c050f3feb
//        ed25519_pk1cge7u3fmufeeyx3k3pawhapgmfqttnt08u3xhfh4axpccpg08l4span0rg
//f3549d1c630df3f36e951b44e42ebd02f92241cf7ca17c6a3c030f89
//        addr_vkh17d2f68rrphelxm54rdzwgt4aqtujysw00jshc63uqv8cjke94ja


//{
//        "publicKey": "c233ee453be273921a36887aebf428da40b5cd6f3f226ba6f5e9838c050f3feb",
//        "publicKeyHash": "f3549d1c630df3f36e951b44e42ebd02f92241cf7ca17c6a3c030f89",
//        "dRepIDCip105": "drep17d2f68rrphelxm54rdzwgt4aqtujysw00jshc63uqv8cjvztap0"
//        }

//COSESign1 coseSign1 = dataSignature.coseSign1();
//byte[] addressBytes = coseSign1.headers()._protected().getAsHeaderMap().otherHeaderAsBytes("address");
//Address address = new Address(addressBytes);
//boolean addressVerified = AddressProvider.verifyAddress(address, pubKey);