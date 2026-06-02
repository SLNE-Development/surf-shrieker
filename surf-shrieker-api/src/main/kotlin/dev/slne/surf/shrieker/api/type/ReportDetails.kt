package dev.slne.surf.shrieker.api.type

enum class ReportDetails(val displayName: String, val description: String) {
    TROLLING_DESCRIPTION(
        "Beschreibung des Vorfalls",
        "Beschreibe den Vorfall so genau wie möglich. Je mehr Details du angibst, desto besser können wir den Fall untersuchen."
    ),
    GRIEF_LOCATION(
        "Ort des Vorfalls",
        "Bitte gebe hier genaue Koordinaten sowie einen optionalen Radius an. Je genauer die Angaben sind, desto besser können wir den Fall untersuchen."
    ),
    GRIEF_ALLOWED(
        "Wer ist in deinem Team?",
        "Gebe hier an, welche Personen in deinem Team sind, damit wir besser einschätzen können, ob es sich um Griefing handelt oder nicht."
    ),
    GRIEF_DESCRIPTION(
        "Beschreibung des Vorfalls",
        "Beschreibe den Vorfall so genau wie möglich. Je mehr Details du angibst, desto besser können wir den Fall untersuchen."
    ),
    EXPLOITING_DESCRIPTION(
        "Beschreibung des Vorfalls",
        "Beschreibe den Vorfall so genau wie möglich. Je mehr Details du angibst, desto besser können wir den Fall untersuchen."
    ),
    VOICE_DESCRIPTION(
        "Beschreibung des Vorfalls",
        "Beschreibe den Vorfall so genau wie möglich. Je mehr Details du angibst, desto besser können wir den Fall untersuchen."
    );

    companion object {
        fun byDataType(reportType: ReportType) = when (reportType) {
            ReportType.TROLLING -> listOf(TROLLING_DESCRIPTION)
            ReportType.GRIEF -> listOf(GRIEF_ALLOWED, GRIEF_DESCRIPTION, GRIEF_LOCATION)
            ReportType.VOICE -> listOf(VOICE_DESCRIPTION)
            ReportType.EXPLOITING -> listOf(EXPLOITING_DESCRIPTION)
            else -> emptyList()
        }
    }
}