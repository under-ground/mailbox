package mailbox.listeners;

import mailbox.GuildUtil;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;


public class ReactionActions implements ReactionAddListener {


    /**
     * Used to monitor if someone clicks the ❌ reaction applied to all messages in the inbox.
     * This allows for easier monitoring for staff members to see when a message has been handled.
     *
     * @param event
     */
    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        if (event.getServer().isPresent()) {
            event.getApi().getThreadPool().getExecutorService().submit(() -> handleReactions(event));
        }
    }

    private void handleReactions(ReactionAddEvent event) {
        if (!event.getUser().isBot() && event.getChannel().getId() == Long.parseLong(GuildUtil.getInboxChannelId(event.getServer().get().getId(), event.getApi()))) {
            if (event.getEmoji().asUnicodeEmoji().orElseThrow(() -> new AssertionError("This should not be triggered by custom emoji")).equals("❌")) {
                event.requestMessage().thenAccept(message -> {
                    message.removeAllReactions();
                    message.addReaction("✅");
                });
            } else if (event.getEmoji().asUnicodeEmoji().orElseThrow(() -> new AssertionError("This should not be triggered by custom emoji")).equals("✅")) {
                event.requestMessage().thenAccept(message -> {
                    message.removeAllReactions();
                    message.addReaction("❌");
                });
            }
        }

    }
}

