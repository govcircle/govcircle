package gov.govcircle.comon.security.provider;


import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.cip.cip30.CIP30DataSigner;
import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.cip.cip8.COSESign1;
import gov.govcircle.comon.security.model.dto.UserAddressSignatureAuthenticationToken;
import gov.govcircle.comon.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.comon.security.model.exception.UserAddressVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserAddressSignatureAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String keyString = authentication.getDetails().toString();
        String signatureString = authentication.getCredentials().toString();
        DataSignature dataSignature = new DataSignature(
                signatureString,
                keyString
        );
        COSESign1 frontEndSignature = dataSignature.coseSign1();

        byte[] addressBytes = frontEndSignature.headers()._protected().getAsHeaderMap().otherHeaderAsBytes("address");
        Address address = new Address(addressBytes);
        String bech32Address = address.toBech32();

        UserDetailsInfoDTO userDetailsInfo = (UserDetailsInfoDTO) userDetailsService.loadUserByUsername(bech32Address);
        boolean verified =
                Objects.equals(
                        userDetailsInfo.getNounc(),
                        new String(frontEndSignature.payload())
                )
                        && CIP30DataSigner.INSTANCE.verify(dataSignature);

        if (!verified) {
            throw new UserAddressVerificationException("the provided data signature is invalid");

        }
        return createAuthentication(
                signatureString,
                keyString,
                userDetailsInfo
        );

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAddressSignatureAuthenticationToken.class);

    }

    private static UserAddressSignatureAuthenticationToken createAuthentication(
            String signature,
            String key,
            UserDetails userDetails
    ) {
        UserAddressSignatureAuthenticationToken authenticationToken = new UserAddressSignatureAuthenticationToken(
                signature,
                key,
                userDetails
        );
        authenticationToken.setAuthenticated(true);
        return authenticationToken;

    }
}

