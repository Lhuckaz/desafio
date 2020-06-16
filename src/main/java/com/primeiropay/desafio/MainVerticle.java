package com.primeiropay.desafio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import com.primeiropay.desafio.model.Client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.impl.ChainAuthHandlerImpl;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.route("/api/v1/preauth").method(HttpMethod.POST).handler(this::pa);

		router.route().handler(BodyHandler.create());
		router.route("/api/v1/capture:id").method(HttpMethod.POST).handler(this::capture);
		
		router.route().handler(BodyHandler.create());
		router.route("/api/v1/refund").method(HttpMethod.POST).handler(this::refund);

		vertx.createHttpServer().requestHandler(router).listen(8888);
	}

	private void pa(RoutingContext routingContext) {
		JsonObject jsonSubset = new JsonObject();
		final Client client = Json.decodeValue(routingContext.getBodyAsString(), Client.class);
		HttpServerRequest request = routingContext.request();
		String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
		String[] parts = authorization.split(" ");
		String token = parts[1];
		try {
			String requestPA = this.requestPA(client, token);

			JsonObject jsonResponse = new JsonObject(requestPA);
			jsonSubset.put("id", jsonResponse.getString("id"));
			jsonSubset.put("result", jsonResponse.getJsonObject("result"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(jsonSubset));
	}

	private void capture(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		JsonObject jsonSubset = new JsonObject();
		final Client client = Json.decodeValue(routingContext.getBodyAsString(), Client.class);
		HttpServerRequest request = routingContext.request();
		String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
		String[] parts = authorization.split(" ");
		String token = parts[1];
		try {
			String requestPA = this.requestCapture(id, client, token);

			JsonObject jsonResponse = new JsonObject(requestPA);
			jsonSubset.put("id", jsonResponse.getString("id"));
			jsonSubset.put("result", jsonResponse.getJsonObject("result"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(jsonSubset));
	}
	
	private void refund(RoutingContext routingContext) {
		JsonObject jsonSubset = new JsonObject();
		final Client client = Json.decodeValue(routingContext.getBodyAsString(), Client.class);
		HttpServerRequest request = routingContext.request();
		String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
		String[] parts = authorization.split(" ");
		String token = parts[1];
		try {
			String requestPA = this.requestPA(client, token);

			JsonObject jsonResponse = new JsonObject(requestPA);
			jsonSubset.put("id", jsonResponse.getString("id"));
			jsonSubset.put("result", jsonResponse.getJsonObject("result"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(jsonSubset));
	}

	private String requestPA(Client client, String token) throws IOException {
		URL url = new URL("https://test.oppwa.com/v1/payments");

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		String data = "" + "entityId=" + client.getEntityId() + "&amount="
				+ String.format("%.2f", client.getAmount()).replace(",", ".") + "&currency=EUR" + "&paymentBrand="
				+ client.getCardBrand() + "&paymentType=DB" + "&card.number=" + client.getCardNumber() + "&card.holder="
				+ client.getCardHolder() + "&card.expiryMonth=" + client.getCardExpiryMonth() + "&card.expiryYear="
				+ client.getCardExpiryYear() + "&card.cvv=" + client.getCardCvv();

		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		InputStream is;

		if (responseCode >= 400)
			is = conn.getErrorStream();
		else
			is = conn.getInputStream();

		return IOUtils.toString(is);
	}

	private String requestCapture(String id, Client client, String token) throws IOException {
		URL url = new URL("https://test.oppwa.com/v1/payments/" + id);

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		String data = "" + "entityId=" + client.getEntityId() + "&amount=" + client.getAmount() + "&currency=EUR"
				+ "&paymentType=CP";

		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		InputStream is;

		if (responseCode >= 400)
			is = conn.getErrorStream();
		else
			is = conn.getInputStream();

		return IOUtils.toString(is);
	}
	
	private String requestRefund(Client client, String token) throws IOException {
		URL url = new URL("https://test.oppwa.com/v1/payments");

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		String data = "" + "entityId=" + client.getEntityId() + "&amount="
				+ String.format("%.2f", client.getAmount()).replace(",", ".") + "&currency=EUR" + "&paymentBrand="
				+ client.getCardBrand() + "&paymentType=CD" + "&card.number=" + client.getCardNumber() + "&card.holder="
				+ client.getCardHolder() + "&card.expiryMonth=" + client.getCardExpiryMonth() + "&card.expiryYear="
				+ client.getCardExpiryYear() + "&card.cvv=" + client.getCardCvv();

		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		InputStream is;

		if (responseCode >= 400)
			is = conn.getErrorStream();
		else
			is = conn.getInputStream();

		return IOUtils.toString(is);
	}
}
