### Description
ZenChatHistory is a plugin for [Velocity](https://velocitypowered.com/), a Minecraft proxy server. 
Since recent updates of minecraft (1.20.2), chat session is cleared on client side when changing backend server while using a proxy such as Velocity, Bungee, etc.
ZenChatHistory is a plugin that allows you to keep your chat history when changing backend server for client using 1.20.2 or higher.

### Dependency
To use ZenChatHistory, you need to have [PacketEvents v2.2.0](https://github.com/retrooper/packetevents/releases/tag/v2.2.0) installed on your velocity proxy.

### Installation
To install ZenChatHistory, just drop the jar file in your `plugins` folder of your velocity proxy.
For now, ZenChatHistory is not available on any plugin repository, builds can be found in the release tab of this project.
This plugin is only compatible with Velocity 3.3.0-SNAPSHOT.
I will make it compatible for other proxy servers in the future, such as BungeeCord or Waterfall.


### Compilation
ZenChatHistory is built with [Gradle](https://gradle.org/). If you have it installed, just run
`./gradlew build` in the root project folder.
Then, you can find the compiled jar in `build/libs/`.
