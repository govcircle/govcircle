package gov.govcircle.common.security.model.entity;

import com.bloxbean.cardano.yaci.core.model.certs.CertificateType;

public enum RoleRegistrationStatus {
    REGISTER(CertificateType.REG_DREP_CERT),
    UNREGISTER(CertificateType.UNREG_DREP_CERT),
    UPDATE(CertificateType.UPDATE_DREP_CERT),;

    private final CertificateType certificateType;

    RoleRegistrationStatus(CertificateType certificateType) {
       this.certificateType = certificateType;

    }
    public CertificateType certificateType() {
        return certificateType;

    }
    public static RoleRegistrationStatus fromRegistrationStatus(CertificateType certificateType) {
        for (RoleRegistrationStatus status : RoleRegistrationStatus.values()) {
            if (status.certificateType().equals(certificateType)) {
                return status;

            }

        }
        throw new IllegalArgumentException("Unknown cert type: " + certificateType);

    }

}
