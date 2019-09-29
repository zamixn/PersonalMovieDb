package com.zkadisa.personalmoviedb.DataHandling;

import java.io.Serializable;

public class SearchResult implements Serializable {
    public SearchEntry[] Search;
    public int totalResults;
    public Boolean Response;
}
