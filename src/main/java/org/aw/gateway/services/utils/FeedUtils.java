package org.aw.gateway.services.utils;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FeedUtils {
    /**
     * Parse comma-separated fields string into a Set
     */
    public Set<String> parseFields(String fields) {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(fields.split(","))
                     .map(String::trim)
                     .filter(f -> !f.isEmpty())
                     .collect(Collectors.toSet());
    }

    /**
     * Validate if requested fields are allowed
     */
    public boolean validateFields(Set<String> requestedFields) {
        Set<String> allowedFields = Set.of(
                "id", "email", "name", "postName", "postContent",
                "comment", "commentedUser", "timestamp", "likes", "shares"
        );
        return allowedFields.containsAll(requestedFields);
    }

    /**
     * Filter feed data to include only requested fields
     */
    public List<Map<String, Object>> filterFeedData(
            List<Map<String, Object>> feedData,
            Set<String> requestedFields) {

        return feedData.stream()
                       .map(item -> filterSingleItem(item, requestedFields))
                       .collect(Collectors.toList());
    }

    /**
     * Filter single feed item to include only requested fields
     */
    public Map<String, Object> filterSingleItem(
            Map<String, Object> item,
            Set<String> requestedFields) {

        Map<String, Object> filtered = new HashMap<>();

        // Always include ID for REST best practices
        if (item.containsKey("id")) {
            filtered.put("id", item.get("id"));
        }

        // Add requested fields
        for (String field : requestedFields) {
            if (item.containsKey(field)) {
                filtered.put(field, item.get(field));
            }
        }

        return filtered;
    }
}
