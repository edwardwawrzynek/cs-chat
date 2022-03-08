# Chat Protocol

A client connects to the server on TCP port 8080. Messages sent between the client and server are each a single line, delimitated with a newline `\n`.

## Client -> Server Messages
## `name NAME...`
Set the connected user's name. The name should not contain spaces.

Example:
```
name Freddy-Knight
```

## `message MESSAGE...`
Send a message. The message may contain spaces. The message should not contain newlines. Multiline messages should be split up into multiple messages that are each a line long.

Example:
```
message Hello World!
```

## `dm NAME MESSAGE`
Send a direct message to a specific user.
Example
```
dm Freddy-Knight Hello, Freddy Knight
```

## `exit`
Disconnect from the server. When the server receives an `exit` message, it will close the connection.

Example:
```
exit
```

## Server -> Client Messages
## `message NAME MESSAGE`
Report that the user `NAME` has sent the message `MESSAGE`. `NAME` will not contain spaces.

Example:
```
message Freddy-Knight Hello World!
```

The message above means that `Freddy-Knight` has sent `Hello World!`.

## `dm NAME MESSAGE`
Report that the user `NAME` has sent the client a dm.