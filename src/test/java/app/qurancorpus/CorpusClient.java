package app.qurancorpus;

import app.qurancorpus.morphology.WordMorphologyResponse;
import app.qurancorpus.orthography.Location;
import app.qurancorpus.orthography.VerseResponse;
import app.qurancorpus.syntax.GraphResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Client("/")
public interface CorpusClient {

    Logger LOG = LoggerFactory.getLogger(CorpusClient.class);

    @Get("metadata")
    default MetadataResponse getMetadata() {
        try {
            return fetchMetadata();
        } catch (HttpClientResponseException e) {
            logError("metadata", e);
            return null;
        }
    }

    @Get("morphology")
    default VerseResponse[] getMorphology(
            @QueryValue @JsonFormat(shape = STRING) Location location,
            @QueryValue("n") int count,
            @Nullable @QueryValue("translation") String translationQuery,
            @Nullable @QueryValue Boolean features) {
        try {
            return fetchMorphology(location, count, translationQuery, features);
        } catch (HttpClientResponseException e) {
            logError("morphology", e);
            return new VerseResponse[0];
        }
    }

    @Get("morphology/word")
    default WordMorphologyResponse getWordMorphology(@QueryValue @JsonFormat(shape = STRING) Location location) {
        try {
            return fetchWordMorphology(location);
        } catch (HttpClientResponseException e) {
            logError("morphology/word", e);
            return null;
        }
    }

    @Get("syntax")
    default GraphResponse getSyntax(
            @QueryValue @JsonFormat(shape = STRING) Location location,
            @QueryValue("graph") int graphNumber) {
        try {
            return fetchSyntax(location, graphNumber);
        } catch (HttpClientResponseException e) {
            logError("syntax", e);
            return null;
        }
    }

    @Get("irab")
    default String[] getIrab(
            @QueryValue @JsonFormat(shape = STRING) Location from,
            @QueryValue @JsonFormat(shape = STRING) Location to) {
        try {
            return fetchIrab(from, to);
        } catch (HttpClientResponseException e) {
            logError("irab", e);
            return new String[0];
        }
    }

    // Actual HTTP methods
    @Get("metadata")
    MetadataResponse fetchMetadata();

    @Get("morphology")
    VerseResponse[] fetchMorphology(Location location, int count, String translationQuery, Boolean features);

    @Get("morphology/word")
    WordMorphologyResponse fetchWordMorphology(Location location);

    @Get("syntax")
    GraphResponse fetchSyntax(Location location, int graphNumber);

    @Get("irab")
    String[] fetchIrab(Location from, Location to);

    private static void logError(String methodName, HttpClientResponseException e) {
        if (e.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
            LOG.error("Service unavailable when calling " + methodName);
        } else {
            LOG.error("Error when calling " + methodName + ": " + e.getMessage());
        }
    }
}
