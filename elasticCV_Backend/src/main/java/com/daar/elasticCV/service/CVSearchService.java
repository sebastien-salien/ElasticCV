package com.daar.elasticCV.service;

import com.daar.elasticCV.model.CV;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CVSearchService {

	private static final String INDEX = "cvs";

	private final ElasticsearchOperations elasticsearchOperations;

	@Autowired
	public CVSearchService(final ElasticsearchOperations elasticsearchOperations) {
		super();
		this.elasticsearchOperations = elasticsearchOperations;
	}

	public CV createCVIndex(CV cv) {
		if(cv.getId().isEmpty() || cv.getId().isBlank()) cv.setId(UUID.randomUUID().toString());
		IndexQuery indexQuery = new IndexQueryBuilder()
				.withId(cv.getId())
				.withObject(cv)
				.build();
		elasticsearchOperations
				.index(indexQuery, IndexCoordinates.of(INDEX));

		return cv;
	}

	public void deleteCVById(String id) {
		elasticsearchOperations
				.delete(id, CV.class);
	}

	public CV findById(String id) {
		return elasticsearchOperations
				.get(id, CV.class);
	}

	public List<CV> getAll() {
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

		Query searchQuery = new NativeSearchQueryBuilder()
				.withFilter(queryBuilder)
				.build();

		SearchHits<CV> cvHits =
				elasticsearchOperations
						.search(searchQuery, CV.class,
								IndexCoordinates.of(INDEX));
		List<CV> cvMatches = new ArrayList<CV>();
		for (SearchHit<CV> searchHit : cvHits) {
			cvMatches.add(searchHit.getContent());
		}
		return cvMatches;
	}

	public List<CV> search(String field, final String query, Operator operator) {
		QueryBuilder queryBuilder = 
				QueryBuilders
				.matchQuery(field, query)
				.operator(operator);

		Query searchQuery = new NativeSearchQueryBuilder()
				                .withFilter(queryBuilder)
				                .build();
		SearchHits<CV> cvHits =
				elasticsearchOperations
				.search(searchQuery, CV.class,
				IndexCoordinates.of(INDEX));
		List<CV> cvMatches = new ArrayList<CV>();
		for (SearchHit<CV> searchHit : cvHits) {
			cvMatches.add(searchHit.getContent());
		}
		return cvMatches;
	}

	public List<CV> findByTitle(final String title) {
		return search("title", title, Operator.OR);
	}

	public List<CV> findBySkill(final String skill) {
		return search("skill", skill, Operator.AND);
	}
}
