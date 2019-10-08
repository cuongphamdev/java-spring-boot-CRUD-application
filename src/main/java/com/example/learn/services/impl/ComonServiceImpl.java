package com.example.learn.services.impl;

import com.example.learn.services.CommonService;

public class ComonServiceImpl implements CommonService {
  @Override
  public int getMaxPagesByTotalItemAndPageBreak(int totalItem, int pageBreak) {
    return totalItem / pageBreak + (totalItem % pageBreak != 0 ? 1 : 0);
  }
}
