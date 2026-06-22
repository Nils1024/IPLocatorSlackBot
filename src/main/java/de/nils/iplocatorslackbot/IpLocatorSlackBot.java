package de.nils.iplocatorslackbot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;

public class IpLocatorSlackBot {
    static void main() {
        App app = new App();

        app.command("/help", (req, ctx) -> {
            return ctx.ack(":wave: Hello!");
        });

        try {
            // SocketModeApp expects an env variable: SLACK_APP_TOKEN
            new SocketModeApp(app).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
