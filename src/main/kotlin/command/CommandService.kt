package command

import command.handler.CommandHandler

class CommandService(private val handler: List<CommandHandler>) {
    private val prefixLength: Int = 4

    private fun isCommand(msg: String) = msg.startsWith("!") || msg.startsWith("?")

    fun isTTSCommand(msg: String) = msg.startsWith("?")

    fun distributeCommand(command: String): String? {
        if (!isCommand(command) || command.length < 5) return null

        val prefix = command.take(prefixLength).takeLast(prefixLength - 1)
        val msg = command.takeLast(command.length - prefixLength - 1)

        handler.forEach {
            if (it.prefix() == prefix) return it.handleCommand(msg)
        }

        return "Something went wrong :/"
    }

    suspend fun distributeEventCommand(eventMsg: String): CommandResponse {
        val command = Command(eventMsg)
        handler.forEach {
            if (it.canHandle(command)) return it.handleInteractionEvent(eventMsg, this)
        }

        throw RuntimeException("No handle found for command.")
    }

}

