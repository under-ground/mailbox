![](https://i.imgur.com/5SiW7vH.png)
# discord-mailbox
A Discord bot written in Java using the Javacord library to provide a simple and easy to use mailbox structure for servers to allow users to message server staff and receive a reply. This project is built for the Discord Community Hack Week for fun and is not intended to be a production ready product.


## Usage

Users can send messages in the designated "Message-Channel" though, the channel name can of course be saved. After the user sends a message, the message is immediately deleted and routed to the inbox channel. Upon successful receipt of the message, the user should receive a direct message confirming their message has been sent. The user will receive a mention ping if their privacy settings do not allow direct messages. 

*Note: It's recommended to enable slowmode to prevent spam*

![](https://media.giphy.com/media/VGJi4ObV5pdJRg8JdT/giphy.gif)


The confirmation message also serves as a copy for the end user to retain for their records.

![](https://i.imgur.com/v2X5bsQ.png)

The "Message-Inbox" channel can also have its name changed without impacting functionality. All received messages are stored here with a red embed color and will alert staff if the user in question has privacy settings restricted.

When replying to a message, the outgoing message is saved as a green embed color. The ability to reply to messages is restricted to the inbox channel which gives freedom for controlling permissions at the server level instead of changing it through the bot. If a staff member replies to a user and that user has restrictive privacy settings, there will be a notification given, though the intended message will be saved for posterity

![](https://media.giphy.com/media/Ti23i1ETaW2vWtCAmG/giphy.gif)

Assuming the end user has corrected their privacy settings, a message will be sent as you'd expect.

![](https://media.giphy.com/media/JUMFPr2fPzIFGqKCqn/giphy.gif)
![](https://i.imgur.com/FwV3Jgs.png)





## Open source libraries used in this project

- [Javacord 3.0.4](https://github.com/Javacord/Javacord)
- [Log4j](https://logging.apache.org/log4j/2.x/manual/index.html)
- [Commons Configuration](https://commons.apache.org/proper/commons-configuration/)


## Logging in

To log into the bot, you will need to place your bot token in runtime arguments depending on the IDE you're using or when running from command line. 
```java
api = new DiscordApiBuilder().setToken(args[0]).login().join();
```

## Credits

- Thanks to everyone over at the [Javacord discord community](https://discord.gg/0qJ2jjyneLEgG7y3) for always being extremely helpful any time I had questions. Saladoc and Vampire the real mvps.

- [Icons8](https://icons8.com/) for logo

## License

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.
