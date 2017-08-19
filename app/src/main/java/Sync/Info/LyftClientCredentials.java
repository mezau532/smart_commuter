package Sync.Info;
/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */
/**
 * Created by umeza on 7/18/17.
 */

public class LyftClientCredentials {
    private String token_type;
    private long access_token;
    private long expires_in;
    private String scope;

    public String getToken_type() { return this.token_type; }
    public void setToken_type(String token_type) { this.token_type = token_type; }
    public String getScope() { return this.scope; }
    public void setScope(String scope) { this.scope = scope; }
    public long getExpires_in() { return this.expires_in; }
    public void setExpires_in(long expires_in) { this.expires_in = expires_in; }
    public long getAccess_token() { return this.access_token; }
    public void setAccess_token(long access_token) { this.access_token = access_token; }
}
