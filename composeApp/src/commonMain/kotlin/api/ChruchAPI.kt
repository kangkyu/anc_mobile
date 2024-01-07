package api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import model.ExternalURL

private const val baseUrl = "https://anc-backend-7502ef948715.herokuapp.com"

class ChruchAPI {

    companion object {
        val shared = ChruchAPI()
    }

    val client = HttpClient(CIO)

    suspend fun getJuboExternalURL(): Result<String> {
        return runCatching {
            val response: HttpResponse = client.get("${baseUrl}/jubo.json")
            client.close()

            response.body()
        }
    }
}

enum class LoadingState {
    Loading, Success, Failure, Error
}
