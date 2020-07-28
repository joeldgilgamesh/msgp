package com.sprintpay.minfi.msgp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Spminfimsgp.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    // @Value("${application.sp-minfi-msss-token}")
    private String spMinfiMsssToken = "";

    public String getSpMinfiMsssToken() {
        return spMinfiMsssToken;
    }

    public void setSpMinfiMsssToken(String spMinfiMsssToken) { this.spMinfiMsssToken = spMinfiMsssToken; }
    
    private String secret;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
    
}
