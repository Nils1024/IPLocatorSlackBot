package de.nils.iplocatorslackbot;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import de.nils.iplocatorslackbot.commands.*;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import de.nils.iplocatorslackbot.services.CommandRegistry;

public class IpLocatorSlackBot {
    static void main() {
        // App expects an env variable: SLACK_BOT_TOKEN
        App app = new App();
        CommandRegistry commandRegistry = new CommandRegistry(app);
        ApiRequestService apiRequestService = new ApiRequestService();

        commandRegistry.register(new HelpCommand(commandRegistry));
        commandRegistry.register(new IpLookupCommand(apiRequestService));
        commandRegistry.register(new DomainLookupCommand(apiRequestService));
        commandRegistry.register(new AsnLookupCommand(apiRequestService));
        commandRegistry.register(new TldLookupCommand(apiRequestService));

        try {
            // SocketModeApp expects an env variable: SLACK_APP_TOKEN
            new SocketModeApp(app).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
