package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PagedResponse<T> {

    @JsonProperty("total_count")
    private long totalCount;

    @JsonProperty("incomplete_results")
    private boolean incompleteResults;

    @JsonProperty("items")
    private List<T> items;

    public PagedResponse() {

    }

    public PagedResponse(long totalCount, boolean incompleteResults, List<T> items) {
        this.totalCount = totalCount;
        this.incompleteResults = incompleteResults;
        this.items = items;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<T> getItems() {
        return items == null ? null : new ArrayList<>(items);
    }

    public final void setItems(List<T> items) {
        if (items == null) {
            this.items = null;
        } else {
            this.items = Collections.unmodifiableList(items);
        }
    }
}