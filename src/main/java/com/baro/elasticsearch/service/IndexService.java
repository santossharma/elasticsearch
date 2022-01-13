package com.baro.elasticsearch.service;

import com.baro.elasticsearch.helper.Indices;
import com.baro.elasticsearch.helper.Util;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class IndexService {
    private final List<String> INDICES_TO_CREATE = List.of(Indices.PASSPORT_INDEX);
    private final static Logger LOG = LoggerFactory.getLogger(IndexService.class);

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public IndexService(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @PostConstruct
    public void tryToCreateIndices() {
        recreateIndices(false);
    }

    public void recreateIndices(final boolean deleteExistingIndices) {
        final String settings = Util.loadAsString("static/es-settings.json");

        if(settings == null) {
            LOG.error("Failed to load Index settings");
            return;
        }
        for (final String indexName : INDICES_TO_CREATE) {
            try {
                boolean indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
                if(indexExists) {
                    if(!deleteExistingIndices) {
                        continue;
                    }
                    // deleting indices from elasticsearch
                    restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
                }

                // Creating new indices
                LOG.info("Creating New Index {}",indexName);
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);

                final String mapping = loadMappings(indexName);
                if (mapping != null) {
                    createIndexRequest.mapping(mapping, XContentType.JSON);
                }

                restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String loadMappings(String indexName) {
        final String mapping = Util.loadAsString("static/mappings/"+ indexName +".json");
        if(mapping == null) {
            return null;
        }
        return mapping;
    }
}
