package mailbox.listeners;


import mailbox.Main;
import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.io.File;
import java.io.IOException;


public class JoinActions implements ServerJoinListener {

    /**
     * Handles events on bot server join
     * @param event
     */
    @Override
    public void onServerJoin(ServerJoinEvent event) {

        File directory = new File("./data/");


        // Verifies directory exists and creates it if not
        if (!directory.exists()){
            directory.mkdir();
        }

        try {

            GuildUtil.configureGuild(event.getServer().getId());

        } catch (ConfigurationException e) {

            Main.logger.info(e.getMessage());

        } catch (IOException e) {

            Main.logger.info(e.getMessage());

        }

    }
}