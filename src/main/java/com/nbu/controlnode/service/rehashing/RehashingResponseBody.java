package com.nbu.controlnode.service.rehashing;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.MoreObjects;

public class RehashingResponseBody {

    Map<String, Object> jsonData;

    public RehashingResponseBody() {
    }

    public RehashingResponseBody(Map<String, Object> jsonData) {
        this.jsonData = jsonData;
    }

    public Map<String, Object> getData() {
        return jsonData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RehashingResponseBody that = (RehashingResponseBody) o;
        return Objects.equals(jsonData, that.jsonData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonData);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("jsonData", jsonData)
                .toString();
    }
}
