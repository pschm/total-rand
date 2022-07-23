import command.handler.ModerWarfare2
import org.junit.jupiter.api.Test

internal class ModerWarfare2Test {
    private val randomMap: String = "map"


    @Test
    fun `random maps should not repeat itself`() {
        val mapCount = ModerWarfare2.baseMaps.size
        val resultList = mutableListOf<String>()

        repeat(mapCount - 1) {
            println("Pick $it. Map:")
            resultList.add(ModerWarfare2.handleCommand(randomMap) ?: "")
        }
        val lastMap = ModerWarfare2.handleCommand(randomMap) ?: ""

        assert(!resultList.contains(lastMap)) { "Picked Duplicated map" }
        assert(!resultList.contains("")) { "Should not contain errors" }
    }

    @Test
    fun `random maps should start repeating itself`() {
        val mapCount = ModerWarfare2.baseMaps.size
        val resultList = mutableListOf<String>()

        repeat(mapCount) {
            println("Pick $it. Map:")
            resultList.add(ModerWarfare2.handleCommand(randomMap) ?: "")
        }
        val lastMap = ModerWarfare2.handleCommand(randomMap) ?: ""

        assert(resultList.contains(lastMap)) { "Picked Duplicated map" }
        assert(!resultList.contains("")) { "Should not contain errors" }
    }
}