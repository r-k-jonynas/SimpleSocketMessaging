# SimpleSocketMessaging
Hi. Welcome to the repo for my experiments with peer-to-peer socket communication on Android. This is a beginning of my attempts to turn a Python TCP socket-based chat prototype into a decent Android app.

## Current stage
As of now, the app successfully sends a message to another app user connected to the same WiFi network. Then the message is displayed as a Toast to the user's phone. Since every user is both a client and a server, they can receive and send messages at the same time.

## Further milestones
1. Fixing threads (currently, Toast message is displayed from the worker thread; I need to give it to a handler and display it on the main aka UI thread).
2. Enabling multiple chat connections at the same time.
3. Connecting this backend to an already written UI for multiple concurrent connection chat.
4. Enabling group chats.
