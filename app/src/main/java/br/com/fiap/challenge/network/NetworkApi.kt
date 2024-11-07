package br.com.fiap.challenge.network


import br.com.fiap.challenge.models.RiskStatus
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val URL = "http://192.168.0.181:8080/api/"

data class LoginDTO(
    val registrationNumber: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val message: String
)


interface AuthService {
    @POST("auth/signIn")
    suspend fun signIn(@Body loginDTO: LoginDTO): Response<AuthResponse>
}

fun createAuthService(): AuthService {
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AuthService::class.java)
}


data class ConsultationResponse(
    val _embedded: Embedded
)

data class Embedded(
    val consultationResponseDTOList: List<ConsultationResponseDTO>
)


data class ConsultationResponseDTO(
    val id: String,
    val consultationDate: String,
    val consultationValue: Double,
    val riskStatus: RiskStatus,
    val description: String?,
    val patient: Patient,
    val dentists: List<Dentist>,
)

data class Patient(
    val id: String,
    val name: String,
    val birthday: String,
    val gender: String,
    val riskStatus: String,
    val consultationFrequency: Int,
    val associatedClaims: String
)

data class Dentist(
    val id: String,
    val name: String,
    val specialty: String,
    val registrationNumber: String,
    val claimsRate: Double,
    val riskStatus: RiskStatus
)




interface ConsultationService{

    @GET("consultations")
    suspend fun getAllConsultations(): Response<ConsultationResponse>

}

fun createConsultationService(): ConsultationService {
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ConsultationService::class.java)
}