package ee.mtiidla.freelane.data.external.mapper

// TODO: marko 2019-06-10 this should be moved to table or configuration file
object OpeningHoursCategory {

    fun getCategoryName(categoryId: String): String {
        return categories[categoryId] ?: ""
    }

    private val categories = mapOf(
        "7447" to "Bassiner",
        "14436" to "Grøn tid",
        "14371" to "for offentlige svømmere",
        "14419" to "Kun friluftsbadet",
        "7379" to "Legetid",
        "7445" to "NB - Kun brusebad og sauna - svømmehallen er lukket",
        "7321" to "NB to 25 m bassin lukket i dag",
        "7446" to "NB! Kun brusebad og sauna - svømmehallen er lukket",
        "14369" to "NB. Bellahøj Friluftsbad er åbent",
        "14370" to "NB. Springbassinet er lukket.",
        "7386" to "Offentlig åbningstid",
        "14367" to "Offentlig åbningstid (NB to 50 meter bassin lukket)",
        "7448" to "Svømmehallens billetsalg",
        "7405" to "Udendørs sauna",
        "7329" to "Kurbad",
        "7328" to "Springtider",
        "7360" to "Livredderbemanding",
        "7320" to "Telefon",
        "7318" to "Åbningstid",
        "7319" to "Selvbetjent åbningstid"
    )
}
