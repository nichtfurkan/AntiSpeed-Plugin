# AntiSpeed-Plugin
AntiSpeed is a Plugin that uses Location checks to detect Speed Hackers.

It is optimized for Vanilla and will throw out false positives if there are any other Plugins that increase the Speed.

The plugin supports only speed one and two because there are no other speeds available in vanilla minecraft.

To make the plugin compatible with other plugins I advise to use the source code. It is very simple to understand and easy to modify.

The plugin was tested with the LiquidBounce client B72 and knocked out a flag at every speed mode.

The method of detection is very simple and not complicated. Nevertheless, it works accurately and trustworthily.
It checks how much distance a player travels to his last position and if it exceeds a certain value, he is flagged. With a few minor changes, false flagging is no longer a common occurrence.
After the 3rd flag you are teleported back and after the 6th you are kicked. Every 6 seconds a flag is removed.
