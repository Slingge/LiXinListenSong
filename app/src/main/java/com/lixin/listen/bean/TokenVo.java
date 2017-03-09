package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/11.
 */

public class TokenVo {


    /**
     * access_token : ms0byHLHtxcbFYP5BA3AjFlj_cnHdWY-Hj3ll7WLJEN0zk7RDPBpdeH9xojgLL0f83JG9t5A9nR1lV1oWsLmr5TQ54y7PXfuh9dBMkLD-xo
     * expires_in : 7200
     * refresh_token : 04RNPp3PcpSwejX8dWMWh_vEHXYwZ5vbAJnL7CZ3pGb60bbmmYjATtHlaTcIqlj30bP72pwWuFwerkZt8aG6IfaAi0CSh5dzQdHFP5bbspw
     * openid : oBc3lvi_9C_RZC6A71r_Tu-kCzzQ
     * scope : snsapi_userinfo
     * unionid : oBk9cwal9J9NR7H6FV243r1fHNDg
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
