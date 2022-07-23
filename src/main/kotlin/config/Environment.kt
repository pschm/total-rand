package config

data class Environment(val token: String) {
    companion object {
        const val FILE_PATH = "/env.yml"
    }
}