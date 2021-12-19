
package matteroverdrive.api;

import matteroverdrive.api.exceptions.CoreInaccessibleException;

import java.lang.reflect.Field;

final class MOApi {
    private static final String CORE_API_CLASS = "matteroverdrive.core.MOAPIInternal";
    private static final String CORE_API_FIELD = "INSTANCE";
    private static MatterOverdriveAPI API_INSTANCE;

    static MatterOverdriveAPI instance() {
        if (API_INSTANCE == null) {
            try {
                final Class<?> apiClass = Class.forName(CORE_API_CLASS);
                final Field apiField = apiClass.getField(CORE_API_FIELD);
                API_INSTANCE = (MatterOverdriveAPI) apiField.get(apiClass);
            } catch (ClassNotFoundException e) {
                throw new CoreInaccessibleException("MatterOverdrive API tried  to access the %s class, without it being declared", CORE_API_CLASS);
            } catch (IllegalAccessException e) {
                throw new CoreInaccessibleException("MatterOverdrive API tried to access the %s field in %s without it being declared.", CORE_API_FIELD, CORE_API_CLASS);
            } catch (NoSuchFieldException e) {
                throw new CoreInaccessibleException("MatterOverdrive API tried to access the %s field in %s without enough access permissions.", CORE_API_FIELD, CORE_API_CLASS);
            }
        }
        return API_INSTANCE;
    }
}