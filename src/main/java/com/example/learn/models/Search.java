package com.example.learn.models;

import java.util.List;

public class Search<T> {

  private List<T> listItems;
  private int totalItems;
  private int maxPages;
  private String searchQuery;
  private String anotherQuery;
  private int currentPage;
  private String sortBy;
  private int pageBreak;
  private int anotherDataId;

  public Search() {
  }

  public Search(List<T> listItems, int totalItems, int maxPages, String searchQuery, int currentPage) {
    this.listItems = listItems;
    this.totalItems = totalItems;
    this.maxPages = maxPages;
    this.searchQuery = searchQuery;
    this.currentPage = currentPage;
  }

  public List<T> getListItems() {
    return listItems;
  }

  public String getAnotherQuery() {
    return anotherQuery;
  }

  public void setAnotherQuery(String anotherQuery) {
    this.anotherQuery = anotherQuery;
  }

  public int getAnotherDataId() {
    return anotherDataId;
  }

  public void setAnotherDataId(int anotherDataId) {
    this.anotherDataId = anotherDataId;
  }

  public int getPageBreak() {
    return pageBreak;
  }

  public void setPageBreak(int pageBreak) {
    this.pageBreak = pageBreak;
  }

  public void setListItems(List<T> listItems) {
    this.listItems = listItems;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public int getMaxPages() {
    return maxPages;
  }

  public void setMaxPages(int maxPages) {
    this.maxPages = maxPages;
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }


}
