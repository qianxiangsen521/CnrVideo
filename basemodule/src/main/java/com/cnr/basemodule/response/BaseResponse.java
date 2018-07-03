package com.cnr.basemodule.response;


import com.cnr.basemodule.entity.StatusUnit;

import java.io.Serializable;

/**
 * Created by hywel on 2017/12/25.
 */

public class BaseResponse implements Serializable {
    StatusUnit status;

    public StatusUnit getStatus() {
        return status;
    }

    public void setStatus(StatusUnit status) {
        this.status = status;
    }

}
