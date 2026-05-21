package gov.govcircle.ogmios;

import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.address.AddressProvider;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.*;
import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class OgmiosClient extends WebSocketClient {

    public static final int CRV_KEY = -1;
    public static final int X_KEY = -2;
    public static final String ADDRESS_KEY = "address";
    public static final int OKP = 1;
    public static final int ALG_EdDSA = -8;
    public static final int CRV_Ed25519 = 6;

    private int currentEpoch = -1;
    private long highestBlockHeight = -1;
    private SSLSocketFactory sslSocketFactory;
    private static Map<String, Object> requestJson = new HashMap<>();
    private static int counter = 1;

    static {
        requestJson.put("jsonrpc", "2.0");
        requestJson.put("method", "queryLedgerState/governanceProposals");
        Map<String, Object> transactionId = Map.of("id", "gov_action1nd3t833j7v5sz65k3tp9yyvztw60sjcjgcgjr37682s3m7frwrusqmd2k80");
        Map<String, Object> transaction = Map.of("transaction", transactionId, "index", 0);
        Map<String, Object> proposals = Map.of("proposals", Collections.singleton(transaction));
        requestJson.put("params", proposals);
        requestJson.put("id", null);


    }

    public OgmiosClient(URI serverUri) throws NoSuchAlgorithmException {
        super(serverUri);

        SSLContext sslContext = SSLContext.getInstance("TLS"); // Or "TLSv1.2", etc.
        try {
            sslContext.init(null, null, null); // Use default trust manager
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.sslSocketFactory = sslContext.getSocketFactory();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to Ogmios!");
//        BackendService backendService = new
        // Send the GetChainTip request immediately after opening the connection
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            String jacksonData = objectMapper.writeValueAsString(requestJson);
//            send(jacksonData);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//
//        }


    }




    @Override
    public void onMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            requestJson.put("id", counter ++);
            String jacksonData = objectMapper.writeValueAsString(requestJson);
            send(jacksonData);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed. Code: " + code + ", Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public int getCurrentEpoch() {
        return currentEpoch;
    }

    public long getHighestBlockHeight() {
        return highestBlockHeight;
    }

    public static void main(String[] args) throws Exception {

        String dataString = "123";
        String addressString = "stake1u96ahmjcmtfysxct82wahl48ynxqzd04xufrcq9u8h92x8gxmgfuj";
        String signatureString = "84584aa3012704581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31d6761646472657373581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31da166686173686564f4433132335840187737ee2c33455e99460ee1f7520ae9c580e53ad26bfad8eefefe99d27eec34af5f9f305ad7d5ed18a68e7b8db2a817bd3804e504a9802c053b3e0c9f8d9c0c";
        String keyString = "a5010102581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31d03272006215820767052b2fade79d7c7c0c0f47ce0acf4821db4acbe98310f8e66105a7f7682f4";

        String json = "{\n" +
                "  \"signature\" : \"84584aa3012704581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31d6761646472657373581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31da166686173686564f4433132335840187737ee2c33455e99460ee1f7520ae9c580e53ad26bfad8eefefe99d27eec34af5f9f305ad7d5ed18a68e7b8db2a817bd3804e504a9802c053b3e0c9f8d9c0c\",\n" +
                "  \"key\" : \"a5010102581de175dbee58dad2481b0b3a9ddbfea724cc0135f537123c00bc3dcaa31d03272006215820767052b2fade79d7c7c0c0f47ce0acf4821db4acbe98310f8e66105a7f7682f4\"\n" +
                "}";
        DataSignature dataSignature = new DataSignature(
                signatureString,
                keyString
        );
        DataSignature fromJson = DataSignature.from(json);
        COSESign1 frontEndSignature = dataSignature.coseSign1();
        COSEKey frontEndKey = dataSignature.coseKey();

        byte[] pubKey = frontEndKey.otherHeaderAsBytes(X_KEY);
        byte[] signature = frontEndSignature.signature();

        frontEndSignature.payload();
        //Verify address
        byte[] addressBytes = frontEndSignature.headers()._protected().getAsHeaderMap().otherHeaderAsBytes(ADDRESS_KEY);
        Address address = new Address(addressBytes);
        Address address1 = new Address("stake1u93n8typtr3fc5hrlns6wy62qslfsq7ue3smu67kd8zdtyqs8esfe");

        COSEKey localKey = new COSEKey()
                .keyType(OKP) //OKP
                .keyId(address1.getBytes())
                .algorithmId(ALG_EdDSA) //EdDSA
                .addOtherHeader(CRV_KEY, new UnsignedInteger(CRV_Ed25519)) //crv Ed25519
                .addOtherHeader(X_KEY, new ByteString(pubKey));  //x pub key


        String localKeyString = HexUtil.encodeHexString(localKey.serializeAsBytes());
        dataSignature = new DataSignature(
                signatureString,
                localKeyString
        );
        boolean addressVerified = AddressProvider.verifyAddress(address1, pubKey);
        boolean s2 = CIP30DataSigner.INSTANCE.verify(dataSignature);
        boolean s3 = CIP30DataSigner.INSTANCE.verify(dataSignature);

        COSESign1 sign = COSESign1.deserialize(HexUtil.decodeHexString(signatureString));
        COSEKey CoseKey = COSEKey.deserialize(HexUtil.decodeHexString(keyString));
    }
}
