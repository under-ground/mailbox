package mailbox.listeners;

import mailbox.Mailbox;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerLeaveListener;

public class ServerLeaveActions implements ServerLeaveListener {

    @Override
    public void onServerLeave(ServerLeaveEvent event) {
        Mailbox.logger.info("Joined server: " + event.getServer().getName() + " - Guild ID: " + event.getServer().getId());
    }

}
