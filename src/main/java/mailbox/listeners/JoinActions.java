package mailbox.listeners;


import mailbox.Main;
import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.io.IOException;


public class JoinActions implements ServerJoinListener {

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        try {
            GuildUtil.configureGuild(event.getServer().getId());
        } catch (IOException | ConfigurationException e) {
            Main.logger.info(e.getMessage());

        }

    }
}