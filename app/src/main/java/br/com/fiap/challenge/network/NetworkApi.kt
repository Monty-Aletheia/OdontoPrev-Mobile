package br.com.fiap.challenge.network


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

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