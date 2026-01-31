package com.juma.mobilewalletandroidapplication.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class NetworkRequestTemplates @Inject constructor(
    val client: HttpClient
) {

    suspend inline fun <reified T, reified R> postRequest(
        endPoint: String,
        requestBody: T
    ): Pair<HttpStatusCode, R> {
        val response = client.post(endPoint) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(requestBody)
        }

        return Pair(response.status, response.body())
    }

    suspend inline fun <reified R> getRequest(
        endPoint: String,
        pathVariable: Any? = null,
        queryParams: Map<String, Any?> = emptyMap()
    ): Pair<HttpStatusCode, R> {
        val finalEndPoint = if (pathVariable != null) {
            "$endPoint/$pathVariable"
        } else endPoint

        val response = client.get(finalEndPoint) {
            contentType(ContentType.Application.Json)
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
        }

        val body: R = response.body()
        return Pair(response.status, body)
    }
}