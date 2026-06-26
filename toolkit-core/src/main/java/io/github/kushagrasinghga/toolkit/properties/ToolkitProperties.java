package io.github.kushagrasinghga.toolkit.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "toolkit")
public class ToolkitProperties {
    private final Logging logging = new Logging();
    private final Audit audit = new Audit();
    private final Response response = new Response();
    private final Timer timer = new Timer();

    public Logging getLogging() {
        return logging;
    }

    public Audit getAudit() {
        return audit;
    }

    public Response getResponse() {
        return response;
    }

    public Timer getTimer() {
        return timer;
    }

    public static class Logging {
        private boolean enabled = true;
        private boolean includeHeaders;
        private boolean includeBody;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isIncludeHeaders() {
            return includeHeaders;
        }

        public void setIncludeHeaders(boolean includeHeaders) {
            this.includeHeaders = includeHeaders;
        }

        public boolean isIncludeBody() {
            return includeBody;
        }

        public void setIncludeBody(boolean includeBody) {
            this.includeBody = includeBody;
        }
    }

    public static class Audit {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Response {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Timer {
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
