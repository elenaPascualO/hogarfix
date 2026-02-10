package com.hogarfix.domain.model

enum class HousingType {
    APARTMENT,
    HOUSE_WITH_GARDEN,
    VILLA_WITH_POOL
}

data class ReminderTemplate(
    val title: String,
    val description: String,
    val category: Category,
    val intervalDays: Int
)

object OnboardingTemplates {

    private val baseTemplates = listOf(
        ReminderTemplate(
            title = "Inspeccion Tecnica del Edificio (ITE)",
            description = "Revision obligatoria del estado del edificio segun normativa vigente",
            category = Category.OTHER,
            intervalDays = 3650 // 10 years
        ),
        ReminderTemplate(
            title = "Revision de gas",
            description = "Inspeccion periodica obligatoria de la instalacion de gas",
            category = Category.PLUMBING,
            intervalDays = 1825 // 5 years
        ),
        ReminderTemplate(
            title = "Revision de caldera",
            description = "Mantenimiento anual obligatorio de la caldera",
            category = Category.HVAC,
            intervalDays = 365 // 1 year
        ),
        ReminderTemplate(
            title = "Limpieza filtros aire acondicionado",
            description = "Limpieza de filtros del sistema de climatizacion",
            category = Category.HVAC,
            intervalDays = 180 // 6 months
        ),
        ReminderTemplate(
            title = "Boletin electrico",
            description = "Certificado de instalacion electrica de baja tension",
            category = Category.ELECTRICAL,
            intervalDays = 7300 // 20 years
        ),
        ReminderTemplate(
            title = "Certificado energetico",
            description = "Renovacion del certificado de eficiencia energetica",
            category = Category.OTHER,
            intervalDays = 3650 // 10 years
        )
    )

    private val gardenTemplates = listOf(
        ReminderTemplate(
            title = "Poda de arboles y setos",
            description = "Mantenimiento semestral de jardin: poda, recorte y limpieza",
            category = Category.GARDEN,
            intervalDays = 180 // 6 months
        ),
        ReminderTemplate(
            title = "Revision sistema de riego",
            description = "Comprobacion anual del sistema de riego automatico",
            category = Category.GARDEN,
            intervalDays = 365 // 1 year
        )
    )

    private val poolTemplates = listOf(
        ReminderTemplate(
            title = "Apertura de piscina",
            description = "Puesta a punto anual de la piscina para temporada de bano",
            category = Category.CLEANING,
            intervalDays = 365 // 1 year
        ),
        ReminderTemplate(
            title = "Revision depuradora piscina",
            description = "Mantenimiento del sistema de depuracion y filtrado",
            category = Category.CLEANING,
            intervalDays = 180 // 6 months
        )
    )

    fun getTemplates(housingType: HousingType): List<ReminderTemplate> {
        return when (housingType) {
            HousingType.APARTMENT -> baseTemplates
            HousingType.HOUSE_WITH_GARDEN -> baseTemplates + gardenTemplates
            HousingType.VILLA_WITH_POOL -> baseTemplates + gardenTemplates + poolTemplates
        }
    }
}
