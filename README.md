# VelocityCommands

A lightweight but powerful Velocity proxy plugin that lets you block, disable, and override commands - with full MiniMessage support.

## Features

- 🚫 **Disable commands** - completely block commands from being executed
- 💬 **Override commands** - replace a command's response with a custom MiniMessage message
- 🔍 **Tab-complete blocking** - hide commands from tab-complete suggestions
- 🛡️ **Colon bypass protection** - blocks tricks like `/bukkit:plugins`
- 🔄 **Hot reload** - reload config without restarting the proxy
- ✏️ **Runtime editing** - add/remove rules in-game via commands

## Commands

| Command | Description | Permission |
|---|---|---|
| `/vcmds reload` | Reload the configuration | `velocitycommands.staff` |
| `/vcmds list` | List all disabled/overridden commands | `velocitycommands.staff` |
| `/vcmds disabled add <command>` | Disable a command | `velocitycommands.staff` |
| `/vcmds disabled remove <command>` | Re-enable a command | `velocitycommands.staff` |
| `/vcmds overridden add <command> <message>` | Override a command with a custom message | `velocitycommands.staff` |
| `/vcmds overridden remove <command>` | Remove a command override | `velocitycommands.staff` |

**Aliases:** `/velocitycommands`, `/vscmd`

## Permissions

| Permission | Description |
|---|---|
| `velocitycommands.staff` | Access to all `/vcmds` subcommands |
| `velocitycommands.bypass` | Bypass all command blocks and overrides |

