package xyz.devcmb.treeTumblers.commands

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.commands.cmd.*
import xyz.devcmb.treeTumblers.commands.completion.*

object CommandManager {
    fun register() {
        registerSingleCommand("playerdata", PlayerDataCommand())
        registerSingleCommand("teamdata", TeamDataCommand())

        registerSingleTabCompletion("teamdata", TeamDataCommandCompletion())
    }

    /**
     * Registers a single command
     * @param command The command name
     * @param executor The command executor class
     */
    fun registerSingleCommand(command: String, executor: CommandExecutor?) = TreeTumblers.plugin.getCommand(command)?.setExecutor(executor)


    /**
     * Registers a single tab completion
     * @param command The command name
     * @param completer The tab completer class
     */
    fun registerSingleTabCompletion(command: String, completer: TabCompleter?) = TreeTumblers.plugin.getCommand(command)?.tabCompleter = completer
}