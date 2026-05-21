package gov.govcircle.common.config;

import gov.govcircle.common.security.model.dto.InMemoryActorAuthenticationIdentifierDTO;

import java.util.HashMap;
import java.util.Map;

public class Configs {

    public static String APPLICATION_USE_TERMS_CONSENT = "I agree with terms";

    public static class ROLES {
        public static String SPO_ROLE_TITLE = "SPO";
        public static String SPO_ROLE_DESCRIPTION = "Stake Pool Operator";
        public static Long SPO_ROLE_CODE = 101L;
        public static Long SPO_ROLE_ID = 1L;

        public static String CC_ROLE_TITLE = "CC";
        public static String CC_ROLE_DESCRIPTION = "Constitution Committee";
        public static Long CC_ROLE_CODE = 102L;
        public static Long CC_ROLE_ID = 2L;

        public static String DREP_ROLE_TITLE = "DRep";
        public static String DREP_ROLE_DESCRIPTION = "Delegated Representatives";
        public static Long DREP_ROLE_CODE = 103L;
        public static Long DREP_ROLE_ID = 3L;

        public static String WALLET_ROLE_TITLE = "Wallet";
        public static String WALLET_ROLE_DESCRIPTION = "User Wallet";
        public static Long WALLET_ROLE_CODE = 104L;
        public static Long WALLET_ROLE_ID = 4L;

        public static String ROLE_PREFIX = "ROLE_";

        public static Long AUTHORITY_PROPOSAL_CREATE_ID = 1L;
        public static String AUTHORITY_PROPOSAL_CREATE_TITLE = "create proposal";
        public static String AUTHORITY_PROPOSAL_CREATE_DESCRIPTION = "create proposal";
        public static Long AUTHORITY_PROPOSAL_CREATE_CODE = 1001L;

    }

    public static String API_VERSION = "/V1";
    public static Long DEFAULT_ROLE_ID = 1L;

    public static Map<String, InMemoryActorAuthenticationIdentifierDTO> identifierRequestMap = new HashMap<>();

    public static class URLS {
        // Security endpoints
        public static final String REST_GENERATE_NONCE_ENDPOINT = "/generate-nonce";
        public static final String REST_VERIFY_SIGNATURE_ENDPOINT = "/verify-signature";
        public static final String REST_SIGN_UP_ENDPOINT = "/sign-up";
        public static final String REST_SIGN_IN_ENDPOINT = "/sign-in";
        public static final String REST_PROFILE_PATH = "/profile";
        public static final String REST_ADD_ACTOR_SIGNATURE_ENDPOINT = "/add-actor-signature";
        public static final String REST_UPDATE_PROFILE_ENDPOINT = "/udpate-profile";
        public static final String REST_FREE_ENDPOINT = "/free";
        public static final String REST_NOT_ENDPOINT = "/not";


    }


}