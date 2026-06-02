package dev.slne.surf.shrieker.api.state

enum class ReportProcessState(val text: String) {
    PROCESSING("Der Report wird verarbeitet"),
    COLLECTING_ADDITIONAL_DATA("Weitere Daten werden gesammelt"),
    SAVE_NOTES("Notizen werden gespeichert"),
    SAVING_REPORT("Der Report wird gespeichert"),
    NOTIFYING_STAFF("Das Personal wird benachrichtigt"),
    DONE("Der Report wurde erfolgreich verarbeitet!");
}