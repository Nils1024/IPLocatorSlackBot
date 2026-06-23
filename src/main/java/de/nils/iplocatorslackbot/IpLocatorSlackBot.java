package de.nils.iplocatorslackbot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import de.nils.iplocatorslackbot.commands.BotCommand;
import de.nils.iplocatorslackbot.commands.HelpCommand;
import de.nils.iplocatorslackbot.commands.LookupCommand;
import de.nils.iplocatorslackbot.services.CommandRegistry;

import java.util.List;

public class IpLocatorSlackBot {
    static void main() {
        // App expects an env variable: SLACK_BOT_TOKEN
        App app = new App();
        CommandRegistry commandRegistry = new CommandRegistry(app);

        commandRegistry.register(new HelpCommand());
        commandRegistry.register(new LookupCommand());

        try {
            // SocketModeApp expects an env variable: SLACK_APP_TOKEN
            new SocketModeApp(app).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
