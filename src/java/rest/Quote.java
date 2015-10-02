/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.QuoteNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author Sindt
 */
@Path("quote")
public class Quote {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonParser parser = new JsonParser();

    private static Map<Integer, String> quotes = new HashMap() {
        {
            put(1, "Friends are kisses blown to us by angels");
            put(2, "Do not take life too seriously. You will never get out of it alive");
            put(3, "Behind every great man, is a woman rolling her eyes");
        }
    };

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public Quote() {
    }

    /**
     * Retrieves representation of an instance of rest.Quote
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("{id}")
    public String getJson(@PathParam("id") String id) throws QuoteNotFoundException {
        int newid = Integer.parseInt(id);
//        if (quotes.get(newid) == null) {
//            throw new QuoteNotFoundException("Id findes ikke");
//        }
        String quote = quotes.get(newid);
        if(quote == null){
            throw new IllegalArgumentException("no id " + id);
        }
        JsonObject response = new JsonObject();
        response.addProperty("quote", quote);
        return gson.toJson(response);
    }

    @GET
    @Produces("application/json")
    @Path("random")
    public String getRandomJson() {
        Random random = new Random();
        String quote = quotes.get(random.nextInt(quotes.size())+1);
        JsonObject response = new JsonObject();
        response.addProperty("quote", quote);
        return gson.toJson(response);
    }

    /**
     * PUT method for updating or creating an instance of Quote
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("{id}")
    public String putJson(@PathParam("id") String id, String content) {
        JsonObject request = parser.parse(content).getAsJsonObject();
        String quote = request.get("quote").getAsString();
        int newid = Integer.parseInt(id);
        quotes.put(newid, quote);

        JsonObject response = new JsonObject();
        response.addProperty("id", newid);
        response.addProperty("quote", quote);
        return gson.toJson(response);

    }

    @POST
    public String createQuote(String content) {
        JsonObject request = parser.parse(content).getAsJsonObject();
        String quote = request.get("quote").getAsString();
        quotes.put(quotes.size() + 1, quote);

        JsonObject response = new JsonObject();
        response.addProperty("id", quotes.size());
        response.addProperty("quote", quote);
        return gson.toJson(response);

    }
}
