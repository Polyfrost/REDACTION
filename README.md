# REDACTION

[![Modrinth downloads](https://img.shields.io/modrinth/dt/Gapz72zt?style=flat&logo=modrinth&label=Modrinth&color=1bd96a)](https://modrinth.com/mod/redaction)
[![CurseForge downloads](https://img.shields.io/curseforge/dt/1147999?style=flat&logo=curseforge&label=CurseForge&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/redaction)

REDACTION replaces the hotbar with a flat bar that spans the width of the screen. It also adds falling snow to inventory screens, a button on the title screen that rejoins your last server, and a server preview in Direct Connect. Every feature is off until you turn it on in the settings.

## Features

### Blackbar

The blackbar draws your hotbar items on a translucent black strip that runs from the left edge of the screen to the right edge. A white square marks the selected slot.

- Change the bar color and the highlight color, each with its own opacity.
- Show the slot numbers 1 to 9 on the items.
- On the Fabric builds the highlight slides between slots, the bar moves off screen while the chat is open, and the spectator menu uses the same bar.

### Snow in inventory screens

REDACTION draws white dots that drift across the screen behind your inventory. The Fabric builds also draw them behind chests and other container screens.

- Set the number of snowflakes between 50 and 1000. The default is 100.
- Change the snow color.
- Draw lines between the snowflakes near your cursor. You can set the line width from 1 to 5 and change the line color.

### Last server button

The title screen gets a button with the address of the last server you joined, next to a narrower Multiplayer button. Click it to connect to that server. Singleplayer worlds do not change the saved address.

### Server preview in Direct Connect

The Direct Connect screen shows a server list entry for the address you type, with the server's icon, description and player count. The entry updates 300 ms after you stop typing.

### Hand item FOV

A slider sets the field of view that Minecraft uses to draw your hand and the item you hold. It goes from 0 to 180. The default is 125, and vanilla uses 70.

## Supported versions

- Minecraft 1.8.9 on Ornithe.
- Minecraft 1.21.1, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1, 26.2 and 26.3 on Fabric.

REDACTION needs [OneConfig](https://modrinth.com/mod/oneconfig) and Fabric Language Kotlin. The Fabric builds also need Fabric API.

## Support

- Report bugs and ask questions on the [Polyfrost Discord](https://polyfrost.org/discord).
- Check [Polyfrost Status](https://status.polyfrost.org/) if a Polyfrost service is down.

## Building

```sh
./gradlew ":26.3:build"
```

Replace `26.3` with any supported Minecraft version. The build needs JDK 21 and JDK 25 installed.
