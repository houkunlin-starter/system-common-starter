package com.houkunlin.common.aop.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDownloadFill {
    private String testName;
    private List<ExcelDownloadBean> list;
}
