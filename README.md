![](https://i.imgur.com/5SiW7vH.png)
# discord-mailbox
A Discord bot written in Java using the Javacord library to provide a simple and easy to use mailbox structure for servers to allow users to message all server staff and receive a response in private messages. The bot can be self hosted for a single server, or hosted on many servers at once. This project was built for the [Discord Community Hack Week](https://blog.discordapp.com/discord-community-hack-week-build-and-create-alongside-us-6b2a7b7bba33) for fun and educational purposes.


You will be able to add the bot to your server and test out its features for the duration of the contest. Once the winners have been announced, the bot will be taken offline and it's recommended to self host. 

You can invite the bot to your server by clicking [here](https://discordapp.com/oauth2/authorize?client_id=591466840371363870&scope=bot&permissions=8)

*Note: If you invite the bot to your server, be sure to add the permissions you want in the message-channel so users can see and send messages in it*


## Usage

Users can send messages in the designated "Message-Channel" though, the channel name can of course be saved. After the user sends a message, the message is immediately deleted and routed to the inbox channel. Upon successful receipt of the message, the user should receive a direct message confirming their message has been sent. The user will receive a mention ping if their privacy settings do not allow direct messages. 

*Note: It's recommended to enable slowmode to prevent spam*


- A confirmation message is sent and also serves as a copy for the end user to retain for their records.

- If character count is too high when sending a message or replying to one, the bot will produce a self destructing message that pings the user and informs them their message is too large. 


- The "Message-Inbox" channel can also have its name changed without impacting functionality. All received messages are stored here with a red embed color and will alert staff if the user in question has privacy settings restricted.

- When replying to a message, the outgoing message is saved as a green embed color. The ability to reply to messages is restricted to the inbox channel which gives freedom for controlling permissions at the server level instead of changing it through the bot. If a staff member replies to a user and that user has restrictive privacy settings, there will be a notification given, though the intended message will be saved for posterity.

- Assuming the end user has corrected their privacy settings, a message will be sent as you'd expect.


## Additional Details

When a server staff member responds to a users inquiry, they can click the ❌ reaction which will force the bot to replace it with a ✅ reaction. This functionality allows for easier tracking for which messages have been responded to and which ones need attention.

![](https://media.giphy.com/media/XHFnOAlkJ2vDLKUsLU/giphy.gif)

**If you didn't notice in the pictures above, the users ID is sent as a second message under the embed. You might be asking "Why do this, it's repetitive?"**

- The answer is that on mobile Discord clients it can be quite difficult to copy the user ID from the embed. As a solution to this, the bot will send a second message including the ID in plain text to allow for easier copying.


Upon joining the server, the bot will automatically create two channels, "*message-channel*" & "*message-inbox*" - Once the channels have been created, the bot will automatically post an embed in the *message-channel* which you can leave there for users to follow it's instructions. The new channels will have permissions restricted for *@everyone* so you will need to configure both the *message-channel* and *message-inbox* channel to the permissions you want them to have (having mods able to view *message-inbox* and @everyone or certain activity roles to view the *message-channel.*

![](https://i.imgur.com/UB06pRv.png)


**Currently supported commands** 

*Note: The setup command is not necessary on server join, when the bot is invited to the server it'll automatically generate the necessary configuration files. If there is a message reset or if the bot is invited while the bot is offline, then the command will need to be used.*

![](https://i.imgur.com/Rw585SA.png)

## Open source libraries used in this project

- [Javacord 3.0.4](https://github.com/Javacord/Javacord)
- [Log4j](https://logging.apache.org/log4j/2.x/manual/index.html)
- [Commons Configuration](https://commons.apache.org/proper/commons-configuration/)


## Logging in

To log into the bot, you will need to place your bot token in command line arguments or through usage of an environment variable. Depending on which IDE you use, this can be pretty painless. For Intellij you can do both command line arguments or configure environment variables in Run -> Edit Configurations.
```java
    // The code below will check for cli argument or environment variable and close if neither are found
String token = null;
if (args.length > 0) {
    token = args[0];
}
if (token == null) {
    token = System.getenv("MAILBOX_TOKEN");
}
if (token == null) {
    System.err.println("No Token supplied.");
    System.err.println("Supply Token als Command Line Argument or Environment Variable \"MAILBOX_TOKEN\"");
    System.exit(1);
}

// Use bot token in command arguments or environment variables to run bot.
new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
    api.addServerJoinListener(new JoinActions());
    api.addMessageCreateListener(new MessageActions());
    api.addReactionAddListener(new ReactionActions());
}).exceptionally(ExceptionLogger.get());
```

## Credits

- Thanks to everyone over at the [Javacord discord community](https://discord.gg/0qJ2jjyneLEgG7y3) for always being extremely helpful any time I had questions. Saladoc and Vampire the real mvps.

- [Icons8](https://icons8.com/) for logo

## License

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.
