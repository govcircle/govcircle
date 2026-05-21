package gov.govcircle.govcircle.controller;


import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.governance.keys.DRepKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.govcircle.common.config.Configs;
import gov.govcircle.common.config.TestAccountServices;
import gov.govcircle.common.config.TestAccountServices.*;
import gov.govcircle.common.security.model.dto.JWTTokenResponse;
import gov.govcircle.common.security.model.dto.NonceResponse;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import gov.govcircle.common.security.model.vo.AuthenticationRequest;
import gov.govcircle.common.security.model.vo.DataSignatureVO;
import gov.govcircle.common.security.model.vo.NonceRequest;
import gov.govcircle.common.security.service.JWTService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void initializeAccountsMap() {
        TestAccountServices.fetchGovcircleAccountList();

    }

    @Test
    void walletAuthentication() throws Exception {
        Optional<GovcircleAccount> walletContainer = TestAccountServices.getAccountWithNo(2);
        assertTrue(
                "test wallet dose not exists",
                walletContainer.isPresent()
        );
        GovcircleAccount govcircleAccount = walletContainer.get();
        Account account = govcircleAccount.getAccount();
        String paymentAddress = govcircleAccount.getPaymentAddress();
        authenticateUser(
                CardanoActorType.WALLET,
                account.privateKeyBytes(),
                account.publicKeyBytes(),
                Bech32.decode(paymentAddress).data,
                paymentAddress
        );


    }

    @Test
    void dRepAuthentication() throws Exception {
        Optional<GovcircleAccount> dRepContainer = TestAccountServices.getAccountWithNo(2);
        assertTrue(
                "test dRep dose not exists",
                dRepContainer.isPresent()
        );
        GovcircleAccount govcircleAccount = dRepContainer.get();
        DRepKey dRepAccount = govcircleAccount
                .getAccount()
                .drepKey();
        String cip105DRepId = govcircleAccount.getCip105DRepId();
        authenticateUser(
                CardanoActorType.DREP,
                dRepAccount.signingKey(),
                dRepAccount.verificationKey(),
                Bech32.decode(cip105DRepId).data,
                cip105DRepId
        );


    }

    @Test
    void spoAuthentication() throws Exception {
        Optional<GovcircleSPO> spoContainer = TestAccountServices.getSPOWithNo(2);
        assertTrue(
                "test spo dose not exists",
                spoContainer.isPresent()
        );
        GovcircleSPO govcircleSPO = spoContainer.get();
        String poolId = govcircleSPO.getPoolId();
        authenticateUser(
                CardanoActorType.SPO,
                govcircleSPO.getSigningKeyBytes(),
                govcircleSPO.getVerificationKeyBytes(),
                Bech32.decode(poolId).data,
                poolId
        );


    }

    private void authenticateUser(
            CardanoActorType actorType,
            byte[] signingKeyBytes,
            byte[] verificationKeyBytes,
            byte[] identifierBytes,
            String accountIdentifier
    ) throws Exception {
        NonceRequest nonceRequest = new NonceRequest(
                actorType,
                accountIdentifier
        );
        MvcResult generateNonceResult = mockMvc.perform(post(Configs.URLS.REST_GENERATE_NONCE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorType").value(actorType))
                .andExpect(
                        jsonPath(
                                "$.nonce",
                                matchesPattern("^[0-9a-fA-F-]:" + Configs.APPLICATION_USE_TERMS_CONSENT + ":[0-9a-fA-F-]{36}$")
                        )
                )
                .andReturn();
        NonceResponse nonceResponse = objectMapper.readValue(
                generateNonceResult
                        .getResponse()
                        .getContentAsString(),
                NonceResponse.class
        );
        DataSignature userSignature = CIP30DataSigner.INSTANCE.signData(
                identifierBytes,
                nonceResponse
                        .getNonce()
                        .getBytes(StandardCharsets.UTF_8),
                signingKeyBytes,
                verificationKeyBytes
        );
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                new DataSignatureVO(
                        userSignature.signature(),
                        userSignature.key()
                )
        );
        MvcResult verifySignatureResult = mockMvc.perform(
                        post(
                                Configs.URLS.REST_GENERATE_NONCE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authenticationRequest)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        String jsonString = verifySignatureResult
                .getResponse()
                .getContentAsString();
        JWTTokenResponse jwtTokenResponse = objectMapper.readValue(
                jsonString,
                JWTTokenResponse.class
        );
        String tokenString = jwtTokenResponse.getToken();
        String accountIdentifierFromToken = jwtService.extractUserAddress(tokenString);
        List<String> userAuthoritiesFromToken = jwtService.extractUserAuthorities(tokenString);
        assertEquals(
                "Token " + actorType.title() + "identifier dose not match the request input identifier",
                accountIdentifierFromToken,
                accountIdentifier
        );
        assertTrue(
                actorType.title() + " user dose not have the " + Configs.ROLES.ROLE_PREFIX + actorType.title() + " authority",
                userAuthoritiesFromToken.contains(Configs.ROLES.ROLE_PREFIX + actorType.title())
        );

    }

}
