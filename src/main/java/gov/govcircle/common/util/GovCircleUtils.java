package gov.govcircle.common.util;

import gov.govcircle.core.entities.BaseEntity;

import java.util.Objects;

public class GovCircleUtils {

    public static boolean isNullOrEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();

    }

    public static <T extends BaseEntity> boolean doseEntityExistsInDB(T entity) {
        return Objects.nonNull(entity.getId()) && entity.getId() > 0;

    }

}
