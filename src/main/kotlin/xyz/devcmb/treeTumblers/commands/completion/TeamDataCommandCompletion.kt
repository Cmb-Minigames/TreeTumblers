package xyz.devcmb.treeTumblers.commands.completion

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import xyz.devcmb.treeTumblers.data.DataManager

class TeamDataCommandCompletion : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?>? {
        if(args.size == 1) {
            return DataManager.teamData.keys.toList()
        }

        return listOf()
    }
}