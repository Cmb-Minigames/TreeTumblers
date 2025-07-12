package xyz.devcmb.treeTumblers.commands.cmd

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.devcmb.treeTumblers.data.DataManager
import xyz.devcmb.treeTumblers.util.Format

class TeamDataCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(args.isEmpty()) {
            sender.sendMessage(Format.format("Usage: /teamdata <team id>", Format.FormatType.INVALID))
            return true
        }

        val teamId: String = args[0]
        if(!DataManager.teamData.containsKey(teamId)) {
            sender.sendMessage(Format.format("Team not found", Format.FormatType.INVALID))
            return true
        }

        val data: DataManager.TeamData = DataManager.teamData[teamId]!!

        val comp: Component = Component.empty()
            .append(Component.text(Format.stringToUnicode(data.icon)).font(Key.key("tumbling:icons")))
            .append(Component.text(" ${data.displayName}").color(TextColor.fromHexString(data.color)))
            .append(Component.newline())
            .append(Component.newline())
            .append(Component.text("ID: ").color(NamedTextColor.AQUA))
            .append(Component.text(teamId))
            .append(Component.newline())
            .append(Component.text("Score: ").color(NamedTextColor.YELLOW))
            .append(Component.text(data.score))
            .append(Component.newline())
            .append(Component.text("Team members: ").color(NamedTextColor.GREEN))
            .append(Component.text(data.players.size))
            .append(Component.newline())
            .append(Component.text("Next autosave in ").color(NamedTextColor.RED))
            .append(Component.text(DataManager.getNextAutosaveTime()))

        sender.sendMessage(comp)

        return true
    }

}