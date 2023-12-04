object CalculoChatBotMensagem {

    fun calcularMatematica(equation: String): Int {

        val newEquation = equation.replace(" ", "")

        return when {
            newEquation.contains("+") -> {
                val split = newEquation.split("+")
                val result = split[0].toInt() + split[1].toInt()
                result
            }

            newEquation.contains("-") -> {
                val split = newEquation.split("-")
                val result = split[0].toInt() - split[1].toInt()
                result
            }

            newEquation.contains("*") -> {
                val split = newEquation.split("*")
                val result = split[0].toInt() * split[1].toInt()
                result
            }

            newEquation.contains("/") -> {
                val split = newEquation.split("/")
                val result = split[0].toInt() / split[1].toInt()
                result
            }

            else -> {
                0
            }
        }
    }
}