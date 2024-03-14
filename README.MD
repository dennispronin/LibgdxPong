## JavaFXPong

This is yet another multiplayer Pong implementation.

Game library used: [libGDX](https://libgdx.com/)

Networking library used: [Kryo](https://github.com/EsotericSoftware/kryo)

### How to run:

1. To run client execute the <code>./gradlew run</code> command
2. To run server execute the <code>./gradlew runServer</code> command

### How to play:
1. Start the application
2. Enter server's IP address. By default, address of my server is used
3. Enter server's port. By default, port of my server is used
4. Enter session's ID provided to you by host player if you want to join session. If you are creating session, skip this step
5. Enter session's password
6. Hit "Create session" if you want to be the host player and sen the generated session ID to other player
7. Hit "Join session" if you are connection to existing session

### How it works:
There is a set of events that the client and server can exchange, which can alter the state of both. The server manages the state of a game session, including the connection of two players and their scores. Additionally, the server acts as a mediator, receiving information about the position of a player's paddle and transmitting it to the other player. I have also hardcoded a 60-minute lifetime limit for each game session to prevent the heap from becoming overbloated.

### Known bugs:
Player is able to join his own session