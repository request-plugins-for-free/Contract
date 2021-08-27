package me.tofpu.contract.data.path;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.config.ConfigType;
import me.tofpu.contract.data.path.type.PathType;

public class Path {
    private static final String SETTINGS = "settings.";
    private static final String SETTINGS_CONTRACT = SETTINGS + "contract.";
    private static final String SETTINGS_CHAT = SETTINGS + "chat.";

    // settings
    public static final Value<Integer> SETTINGS_EXPIRE_ON = new Value<>(SETTINGS_CONTRACT + "expire-on", ConfigType.INTEGER, PathType.CONFIG);

    // chat
    public static final Value<Boolean> SETTINGS_CHAT_DISABLE = new Value<>(SETTINGS_CHAT + "disable", ConfigType.BOOLEAN, PathType.CONFIG);
    public static final Value<String> SETTINGS_CHAT_FORMAT = new Value<>(SETTINGS_CHAT + "format", ConfigType.STRING, PathType.CONFIG);

    private static final String STANDARD = "standard.";
    private static final String STANDARD_CONTACT = STANDARD + "contract.";
    private static final String STANDARD_REQUEST = STANDARD + "request.";
    private static final String STANDARD_RATE = STANDARD + "rate.";

    public static final Value<String> STANDARD_CONTRACT_SENT_TO = new Value<>(STANDARD_CONTACT + "sent-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_CONTRACT_SENT_FROM = new Value<>(STANDARD_CONTACT + "sent-from", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> STANDARD_CONTRACT_COMPLETED_TO = new Value<>(STANDARD_CONTACT + "completed-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_CONTRACT_COMPLETED_FROM = new Value<>(STANDARD_CONTACT + "completed-from", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_CONTRACT_EXPIRY = new Value<>(STANDARD_CONTACT + "expired", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> STANDARD_REQUEST_ACCEPTED_TO = new Value<>(STANDARD_REQUEST + "accepted-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_REQUEST_ACCEPTED_FROM = new Value<>(STANDARD_REQUEST + "accepted-from", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> STANDARD_REQUEST_DENIED_TO = new Value<>(STANDARD_REQUEST + "denied-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_REQUEST_DENIED_FROM = new Value<>(STANDARD_REQUEST + "denied-from", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> STANDARD_RATE_RATED_TO = new Value<>(STANDARD_RATE + "rated-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> STANDARD_RATE_RATED_FROM = new Value<>(STANDARD_RATE + "rated-from", ConfigType.STRING, PathType.MESSAGES);

    private static final String ERROR = "error.";
    private static final String ERROR_GENERAL = ERROR + "general.";
    private static final String ERROR_CONTRACT = ERROR + "contract.";
    private static final String ERROR_REQUEST = ERROR + "request.";
    private static final String ERROR_RATE = ERROR + "rate.";

    public static final Value<String> ERROR_TARGET_OFFLINE = new Value<>(ERROR_GENERAL + "target-offline", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_GENERAL_INVALID_CONTRACT = new Value<>(ERROR_GENERAL + "invalid-contract", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> ERROR_CONTRACT_SELF = new Value<>(ERROR_CONTRACT + "self", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_CONTRACT_BUSY = new Value<>(ERROR_CONTRACT + "busy", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_CONTRACT_NOT_ENOUGH_FUNDS = new Value<>(ERROR_CONTRACT + "not-enough-funds", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> ERROR_REQUEST_NO_PENDING = new Value<>(ERROR_REQUEST + "no-pending", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_REQUEST_LACKING_FUNDS_TO = new Value<>(ERROR_REQUEST + "lacking-funds-to", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_REQUEST_LACKING_FUNDS_FROM = new Value<>(ERROR_REQUEST + "lacking-funds-from", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> ERROR_RATE_INVALID_RATE = new Value<>(ERROR_RATE + "invalid-rate", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_RATE_ACTIVE_CONTRACT = new Value<>(ERROR_RATE + "active-contract", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> ERROR_RATE_ONLY_EMPLOYER = new Value<>(ERROR_RATE + "only-employer", ConfigType.STRING, PathType.MESSAGES);

    public static class Value<T> {
        private final String path;
        private final ConfigType<?> type;
        private final PathType pathType;

        private T value;

        public Value(final String path, ConfigType<?> type, final PathType pathType){
            this.path = path;
            this.type = type;
            this.pathType = pathType;
            reload();
        }

        public T getValue() {
            return value;
        }

        public T reload(){
            this.value = ConfigAPI.get(pathType.name(), path, type);
            return value;
        }
    }
}
