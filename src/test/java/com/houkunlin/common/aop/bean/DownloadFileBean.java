package com.houkunlin.common.aop.bean;

import com.houkunlin.common.aop.annotation.DownloadFileModel;
import com.houkunlin.common.aop.annotation.DownloadFileName;
import com.houkunlin.common.aop.annotation.DownloadFileObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@DownloadFileModel
@Data
@AllArgsConstructor
public class DownloadFileBean {
    @DownloadFileName
    private String filename;
    @DownloadFileObject
    private Object file;
}
