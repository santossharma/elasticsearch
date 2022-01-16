package com.baro.elasticsearch.service;

import com.baro.elasticsearch.document.Passport;
import com.baro.elasticsearch.helper.Indices;
import com.baro.elasticsearch.search.dto.SearchRequestDTO;
import com.baro.elasticsearch.search.utils.SearchUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PassportService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(PassportService.class);

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public PassportService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public Boolean index (final Passport passport) {
        try {
            final String passportAsString = MAPPER.writeValueAsString(passport);

            final IndexRequest request = new IndexRequest(Indices.PASSPORT_INDEX);
            request.id(String.valueOf(passport.getPpsId()));
            request.source(passportAsString, XContentType.JSON);

            final IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

            return response != null && response.status().equals(RestStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Passport getByPPSId(final Long ppsId) {
        try {
            final GetResponse documentFields = restHighLevelClient.get(new GetRequest(Indices.PASSPORT_INDEX, String.valueOf(ppsId)),
                    RequestOptions.DEFAULT);

            if(documentFields == null) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), Passport.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Passport> search(final SearchRequestDTO searchRequestDTO) {
        final SearchRequest searchRequest = SearchUtils.buildSearchRequest(Indices.PASSPORT_INDEX, searchRequestDTO);

        if(searchRequest == null) {
            LOGGER.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = searchResponse.getHits().getHits();

            final List<Passport> passportList = new ArrayList<>(searchHits.length);
            for(SearchHit hit : searchHits) {
                passportList.add(MAPPER.readValue(hit.getSourceAsString(), Passport.class));
            }

            return passportList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
