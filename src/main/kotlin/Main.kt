import com.sksamuel.hoplite.ConfigLoader
import command.CommandService
import command.handler.ModerWarfare2
import command.handler.CommandHandler
import config.Environment
import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kord.KordMaster

// https://github.com/kordlib/kord
suspend fun main() {
    // fix ktor warning (https://youtrack.jetbrains.com/issue/KTOR-668)
    System.setProperty("io.ktor.random.secure.random.provider", "DRBG")

    val discordEnv = ConfigLoader().loadConfigOrThrow<Environment>(Environment.FILE_PATH)
    val kord = Kord(discordEnv.token)
    val commandHandler = listOf<CommandHandler>(
        ModerWarfare2,
    )

    val commandService = CommandService(commandHandler)
    val kordMaster = KordMaster(commandService)
    kordMaster.initializeCommands(kord)

    kord.on<ChatInputCommandInteractionCreateEvent> { kordMaster.handleInteractionEvent(this) }
    kord.on<MessageCreateEvent> { kordMaster.handleMessageEvent(this) }

    // connect to discord
    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
